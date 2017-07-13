package ru.geekbrains.gviewer.view;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.geekbrains.gviewer.R;
import ru.geekbrains.gviewer.di.ActivityModule;
import ru.geekbrains.gviewer.di.DaggerInfoComponent;
import ru.geekbrains.gviewer.di.InfoComponent;
import ru.geekbrains.gviewer.model.entity.GithubUser;
import ru.geekbrains.gviewer.model.image.ImageLoader;
import ru.geekbrains.gviewer.presenter.InfoPresenter;
import rx.functions.Action2;
import rx.functions.Func1;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

public class InfoActivity
        extends MvpLceViewStateRetainingActivity<View, GithubUser, InfoView, InfoPresenter>
        implements InfoView {

    // todo: rewrite progress/error switching?
    // todo: change colorscheme according to downloaded avatar?

    @BindView(R.id.info_avatar)
    ImageView avatar;
    @BindView(R.id.info_avatar_text_title)
    TextView avatarTitle;
    @BindView(R.id.info_avatar_text_subtitle)
    TextView avatarSubTitle;
    @BindView(R.id.info_avatar_text)
    ViewGroup avatarTextGroup;
    @BindView(R.id.info_appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.info_toolbar)
    Toolbar toolbar;
    @BindView(R.id.info_bar_title)
    TextView barTitle;

    @BindView(R.id.info_bio)
    ViewGroup bioGroup;
    @BindView(R.id.info_bio_text)
    TextView bioText;

    @BindView(R.id.info_company)
    ViewGroup companyGroup;
    @BindView(R.id.info_company_text)
    TextView companyText;

    @BindView(R.id.info_location)
    ViewGroup locationGroup;
    @BindView(R.id.info_location_text)
    TextView locationText;

    @BindView(R.id.info_blog)
    ViewGroup blogGroup;
    @BindView(R.id.info_blog_text)
    TextView blogText;

    @BindView(R.id.info_email)
    ViewGroup emailGroup;
    @BindView(R.id.info_email_text)
    TextView emailText;

    @Inject
    ImageLoader<ImageView> imageLoader;

    private InfoComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        component = createComponentBuilder().build();
        component.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_info);
        ButterKnife.bind(this);
        Log.d("##########", "DebugGViewerApplication LeakCanary");

        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.update) {
                loadData(true);
                return true;
            }
            return false;
        });

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float absolute = Math.abs(verticalOffset);
            float maximum = appBarLayout.getTotalScrollRange();
            float pi = (float) Math.PI;
            // 0 is 'expanded', 1 is 'closed'
            float elapsed = absolute / maximum;
            if (elapsed > .5f) {
                barTitle.setAlpha((float) Math.sin((elapsed - .5f) * pi));
                avatarTextGroup.setAlpha(0f);
            } else {
                avatarTextGroup.setAlpha((float) Math.cos(elapsed * pi));
                barTitle.setAlpha(0f);
            }
        });
       //throw new IllegalStateException("FUBAR");
    }

    protected DaggerInfoComponent.Builder createComponentBuilder() {
        return DaggerInfoComponent.builder().activityModule(new ActivityModule(this));
    }

    @Override
    @NonNull
    protected String getErrorMessage(@NonNull Throwable throwable, boolean pullToRefresh) {
        String message = throwable.getMessage();
        return message == null ? "Unknown error" : message;
    }

    @Override
    @NonNull
    public InfoPresenter createPresenter() {
        return component.presenter();
    }

    @Override
    public void setData(@NonNull GithubUser data) {
        super.setData(data);
        runOnUiThread(() -> {
            // todo: nullcheck
            avatarTitle.setText(data.getName());
            avatarSubTitle.setText('@' + data.getLogin());
            barTitle.setText(data.getName());
            imageLoader.downloadInto(data.getAvatarUrl(), avatar);

            applyText(data.getBio(), bioText, bioGroup);
            applyText(data.getCompany(), companyText, companyGroup, company -> getString(R.string.info_current_company, company));
            applyText(data.getLocation(), locationText, locationGroup);
            applyText(data.getBlogUrl(), blogText, blogGroup, (str, view) -> {
                String host = Uri.parse(str).getHost();
                String html = String.format("<a href='%s'>%s</a>", str, TextUtils.isEmpty(host) ? str : host);
                Spanned spanned;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
                } else {
                    spanned = Html.fromHtml(html);
                }
                view.setText(spanned);
                // todo: handle 'missing package' error?
                view.setMovementMethod(LinkMovementMethod.getInstance());
            });
            applyText(data.getEmail(), emailText, emailGroup);
        });
    }

    private void applyText(String text, TextView view, ViewGroup group) {
        applyText(text, view, group, str -> str);
    }

    private void applyText(String text, TextView view, ViewGroup group, Action2<String, TextView> modifyIfNotEmpty) {
        applyText(text, view, group, str -> str, modifyIfNotEmpty);
    }

    private void applyText(String text, TextView view, ViewGroup group, Func1<String, String> mapIfNotEmpty) {
        applyText(text, view, group, mapIfNotEmpty, (str, textView) -> {
        });
    }

    private void applyText(String text, TextView view, ViewGroup group, Func1<String, String> mapIfNotEmpty,
                           Action2<String, TextView> modifyIfNotEmpty) {
        if (TextUtils.isEmpty(text)) {
            group.setVisibility(View.GONE);
        } else {
            String mapped = mapIfNotEmpty.call(text);
            view.setText(mapped);
            modifyIfNotEmpty.call(mapped, view);
        }
    }

    @Override
    public void showContent() {
        runOnUiThread(super::showContent);
    }

    @Override
    public void showError(Throwable throwable, boolean pullToRefresh) {
        runOnUiThread(() -> super.showError(throwable, pullToRefresh));
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        runOnUiThread(() -> super.showLoading(pullToRefresh));
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadInformation(pullToRefresh);
    }
}