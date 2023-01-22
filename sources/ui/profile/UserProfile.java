package com.terminato.moneymanager.ui.profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.terminato.moneymanager.EditorDialog;
import com.terminato.moneymanager.EditorDialogCallback;
import com.terminato.moneymanager.MainActivity;
import com.terminato.moneymanager.R;
import com.terminato.moneymanager.core.User;

/**
 * A felhasználó profiljának szerkeztését lehetővé tevő oldal.
 */
public class UserProfile extends Fragment {

    /* Az adott oldalon megjelenő elemek (ezekkel tud kapcsolatba lépni a felhasználó a programmal) */
    private TextView userProfileName;
    private TextView userProfileEmail;
    private ImageButton userProfileNameEdit;
    private ImageButton userProfileEmailEdit;

    private EditorDialog editorDialog;

    private User user; // Egy kiválasztott felhasználó

    public UserProfile() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        user = MainActivity.users.get(0);

        editorDialog = new EditorDialog( root.getContext() );

        userProfileName = root.findViewById( R.id.userProfileName);
        userProfileEmail = root.findViewById( R.id.userProfileEmail);
        userProfileNameEdit = root.findViewById( R.id.userProfileNameEdit);
        userProfileEmailEdit = root.findViewById( R.id.userProfileEmailEdit);

        userProfileName.setText( user.getUserDisplayName() );
        userProfileEmail.setText( user.getUserEmail() );

        userProfileNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorDialog.show( userProfileName.getText().toString(), "Név", new EditorDialogCallback() {
                    @Override
                    public void callBack(String data) {
                        userProfileName.setText( data );
                        user.setDisplayName( data );
                        MainActivity.autoSave();
                    }
                });
            }
        });

        userProfileEmailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorDialog.show(userProfileEmail.getText().toString(), "Email", new EditorDialogCallback() {
                    @Override
                    public void callBack(String data) {
                        userProfileEmail.setText( data );
                        user.setEmail( data );
                        MainActivity.autoSave();
                    }
                });
            }
        });

        return root;
    }
}