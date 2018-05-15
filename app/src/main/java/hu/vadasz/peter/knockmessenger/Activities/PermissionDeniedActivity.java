package hu.vadasz.peter.knockmessenger.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.R;

/**
 * This activity is used when the user denies a permission which was required by the application.
 */

public class PermissionDeniedActivity extends BaseActivity {

    @BindView(R.id.permissionDeniedActivity_retryButton)
    ImageButton imageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_denied);
        ButterKnife.bind(this);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearAndStartActivity(new Intent(PermissionDeniedActivity.this, MainScreenActivity.class));
            }
        });
    }
}
