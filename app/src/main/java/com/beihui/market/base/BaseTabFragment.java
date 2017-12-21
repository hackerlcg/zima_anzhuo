package com.beihui.market.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.R;
import com.beihui.market.util.CommonUtils;


public abstract class BaseTabFragment extends BaseComponentFragment {

    private View fakedStatusBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_tab, container, false);
        fakedStatusBar = view.findViewById(R.id.faked_status_bar);
        ViewGroup rootView = (ViewGroup) view.findViewById(R.id.root_view);
        inflater.inflate(getLayoutResId(), rootView, true);
        if (needFakeStatusBar()) {
            fakeStatusBar();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        fakedStatusBar = null;
        super.onDestroyView();
    }

    /**
     * whether BTF should render FakeStatusBar
     *
     * @return true if should, subImplements should overwrite this to determine custom action
     */
    protected boolean needFakeStatusBar() {
        return true;
    }

    void fakeStatusBar() {
        final int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams params = fakedStatusBar.getLayoutParams();
        params.height = statusHeight;
        fakedStatusBar.setLayoutParams(params);
    }
}
