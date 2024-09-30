package cheap.thrills.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cheap.thrills.R;
import cheap.thrills.activities.DrawerActivity;
import cheap.thrills.utils.UniversalOrlandoPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyProfileFragment extends BaseFragment {

    View mRooView;
    @BindView(R.id.tvNameField)
    TextView tvNameField;
    @BindView(R.id.tvEmailId)
    TextView tvEmailId;
    String strUserName = "", strEmail = "", strLastname = "";
    private Unbinder unbinder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRooView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        unbinder = ButterKnife.bind(this, mRooView);
        return mRooView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeToolbarText();
        setWidgetData();
    }


    private void setWidgetData() {
        if (UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.USER_NAME, "") != null) {
            strUserName = UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.USER_NAME, "");
        }
        if (UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.USER_EMAIL, "") != null) {
            strEmail = UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.USER_EMAIL, "");
        }
        if (UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.USER_LAST_NAME, "") != null) {
            strLastname = UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.USER_LAST_NAME, "");
        }

        tvNameField.setText(strUserName + "" + strLastname);
        tvEmailId.setText(strEmail);
    }

    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    private void changeToolbarText() {
        DrawerActivity.homeclicked = 0;
        Intent i = new Intent(getString(R.string.changeToolBarTittle));
        i.putExtra(getString(R.string.title), getString(R.string.Profile));
        getActivity().sendBroadcast(i);
    }

}
