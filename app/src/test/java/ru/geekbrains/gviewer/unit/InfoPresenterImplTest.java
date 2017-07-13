package ru.geekbrains.gviewer.unit;

import org.junit.Test;
import org.mockito.Mockito;
import ru.geekbrains.gviewer.model.InfoModel;
import ru.geekbrains.gviewer.model.entity.GithubUser;
import ru.geekbrains.gviewer.presenter.InfoPresenter;
import ru.geekbrains.gviewer.presenter.InfoPresenterImpl;
import ru.geekbrains.gviewer.view.InfoView;
import rx.Observable;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;

public class InfoPresenterImplTest {

  @Test
  public void emptyViewAtAttach() {
    emptyViewAtAttach(Observable.empty());
    emptyViewAtAttach(Observable.just(Collections.emptyList()));
  }

  private void emptyViewAtAttach(Observable<List<GithubUser>> emptyResult) {
    InfoView view = mock(InfoView.class);
    InfoModel model = mock(InfoModel.class);
    Mockito.doReturn(emptyResult).when(model).lifecycle();
    InfoPresenter presenter = new InfoPresenterImpl(model);
    presenter.attachView(view);
    Mockito.verifyZeroInteractions(view);
  }

  @Test
  public void contentViewAtAttach() {
    InfoView view = mock(InfoView.class);
    InfoModel model = mock(InfoModel.class);
    GithubUser user = new GithubUser();
    Mockito.doReturn(Observable.just(Collections.singletonList(user))).when(model).lifecycle();
    InfoPresenter presenter = new InfoPresenterImpl(model);
    presenter.attachView(view);
    Mockito.verify(view).setData(user);
    Mockito.verify(view).showContent();
  }

}