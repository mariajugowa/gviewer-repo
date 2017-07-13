package ru.geekbrains.gviewer.instrumented;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import ru.geekbrains.gviewer.di.InfoModelModule;
import ru.geekbrains.gviewer.model.InfoModelImpl;
import ru.geekbrains.gviewer.model.entity.GithubUser;
import rx.observers.TestSubscriber;

import android.support.test.runner.AndroidJUnit4;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class InfoModelImplTest {

  private static MockWebServer mockWebServer;
  private static final String login = "login";
  private static final String avatarUrl = "http://example.com/avatar";
  private static final String name = "user";
  private static final String company = "company";
  private static final String blog = "http://example.com/";
  private static final String location = "location";
  private static final String email = "user@example.com";
  private static final String bio = "bio";

  @Rule
  public Timeout globalTimeout = Timeout.seconds(5);

  @Inject
  InfoModelImpl model;

  @Inject
  RealmConfiguration configuration;

  @Before
  public void prepare() throws IOException {
    DaggerInfoModelTestComponent.builder()
                                .infoModelModule(new InfoModelModule() {
                                  @Override
                                  public String provideEndpoint() {
                                    return mockWebServer.url("/").toString();
                                  }
                                })
                                .build()
                                .inject(this);
  }

  @After
  public void dispose() throws InterruptedException {
    while(Realm.getGlobalInstanceCount(configuration) > 0) {
      Thread.sleep(100);
    }
    Realm.deleteRealm(configuration);
  }

  @Test
  public void emptyAtStart() {
    TestSubscriber<Boolean> sub = TestSubscriber.create();
    model.lifecycle().map(List::isEmpty).first().subscribe(sub);
    sub.awaitTerminalEvent();
    sub.assertValue(true);
  }

  @Test
  public void lifecycleUpdate() {
    mockWebServer.enqueue(createMockResponse(login, avatarUrl, name, company, blog, location, email, bio));

    TestSubscriber<Boolean> sub = TestSubscriber.create();
    model.lifecycle().filter(l -> !l.isEmpty()).map(list -> {
      GithubUser user = list.get(0);
      return user.getLogin().equals(login) && user.getAvatarUrl().equals(avatarUrl)
              && user.getName().equals(name) && user.getCompany().equals(company)
              && user.getBlogUrl().equals(blog) && user.getLocation().equals(location)
              && user.getEmail().equals(email) && user.getBio().equals(bio);
    }).first().subscribe(sub);
    model.updateInfo().subscribe();
    sub.awaitTerminalEvent();
    sub.assertValue(true);
  }

  private MockResponse createMockResponse(String login, String avatarUrl, String name, String company,
                                          String blog, String location, String email, String bio) {
    return new MockResponse().setBody("{\n" +
                                      "\"login\": \"" + login + "\",\n" +
                                      "\"avatar_url\": \"" + avatarUrl + "\",\n" +
                                      "\"name\": \"" + name + "\",\n" +
                                      "\"company\": \"" + company + "\",\n" +
                                      "\"blog\": \"" + blog + "\",\n" +
                                      "\"location\": \"" + location + "\",\n" +
                                      "\"email\": \"" + email + "\",\n" +
                                      "\"bio\": \"" + bio +"\"\n" +
                                      "}");
  }

  @BeforeClass
  public static void setupServer() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @AfterClass
  public static void shutdownServer() throws IOException {
    mockWebServer.shutdown();
  }

}