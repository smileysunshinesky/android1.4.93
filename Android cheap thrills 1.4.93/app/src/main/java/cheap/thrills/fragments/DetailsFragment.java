package cheap.thrills.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cheap.thrills.R;
import cheap.thrills.fonts.TextViewGotham;
import cheap.thrills.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DetailsFragment extends BaseFragment {
    String ticketOrder = "";
    View mRooView;
    @BindView(R.id.tvTicketOrder)
    TextViewGotham tvTicketOrder;
    private Unbinder unbinder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ticketOrder = getArguments().getString(Constants.TICKET_ORDER);
        mRooView = inflater.inflate(R.layout.fragment_details, container, false);
        unbinder = ButterKnife.bind(this, mRooView);
        return mRooView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!ticketOrder.equals(""))
            tvTicketOrder.setText(ticketOrder);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
