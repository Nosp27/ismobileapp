package com.pashikhmin.ismobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;

public class HeaderFragment extends OneShotFragment {
    private HeaderFragmentRequred requester;
    private Context ctx;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.ctx = context;
        requester = (HeaderFragmentRequred) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String tag = getTag();
        View contentView = inflater.inflate(requester.resourceId(tag), container, false);
        if (tag != null)
            ((TextView) contentView.findViewById(R.id.tv_header)).setText(requester.topic(getTag()));
        ((TextView) contentView.findViewById(R.id.tv_user_name)).setText(requester.userName());
        contentView.findViewById(R.id.back).setOnClickListener(e -> ((ComponentActivity) contentView.getContext()).onBackPressed());

        ImageButton notificationBtn = contentView.findViewById(R.id.btn_notification);
        notificationBtn.setOnClickListener(e -> {
            if (Connectors.userAuthorized())
                transitToUpdateFeedOfInvestingFacilities();
            else
                Toast.makeText(ctx, ctx.getString(R.string.not_authorized), Toast.LENGTH_SHORT).show();
        });
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void transitToUpdateFeedOfInvestingFacilities() {
        Criteries criteries = new Criteries();
        criteries.onlyUpdates = true;

        Intent intent = new Intent(ctx, ActivityInvestingFacilities.class);
        intent.putExtra(MainActivity.CRITERIAS_TAG, criteries);
        intent.putExtra("custom_header", ctx.getString(R.string.updates).toLowerCase());
        ctx.startActivity(intent);
    }
}
