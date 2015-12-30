package pl.lukmarr.pokemontrainer.utils.dialogs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.utils.general.RealmUtils;


/**
 *
 */
public class LoginDialog extends AbstractDialog {

    private EditText passwordInput;
    private TextView cancelButton, loginButton;

    public LoginDialog(@Nullable DialogInteractionListener listener) {
        super(listener);
    }

    public LoginDialog prepare(Context context) {
        super.prepareDialog(context);
        dialog.setContentView(R.layout.login_dialog);
        dialog.setCancelable(false);
        passwordInput = (EditText) dialog.findViewById(R.id.password);
        passwordInput.setText(RealmUtils.getUserName(context));
        loginButton = (TextView) dialog.findViewById(R.id.login);
        cancelButton = (TextView) dialog.findViewById(R.id.cancel);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedPassword = passwordInput.getText().toString();
                if (listener != null)
                    listener.afterOk(typedPassword);
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.afterCancel();
                dismiss();
            }
        });
        return this;
    }

    public void dismiss() {
        dialog.dismiss();
        isVisible = false;
    }

    public boolean isShown() {
        return isVisible;
    }
}
