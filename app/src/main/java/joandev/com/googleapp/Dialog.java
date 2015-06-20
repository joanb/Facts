package joandev.com.googleapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by joanbarroso on 16/6/15.
 */
public class Dialog extends DialogFragment implements View.OnClickListener{

    String id;
    LinearLayout linearDate;
    EditText number1;
    EditText number2;
    Button confirmButton;
    TextView dTv;
    TextView textView5;


    static Dialog newInstance(String id) {
        Dialog f = new Dialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("id", id);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        linearDate = (LinearLayout) v.findViewById(R.id.linearDate);

        dTv = (TextView) v.findViewById(R.id.dTv);
        textView5 = (TextView) v.findViewById(R.id.textView5);
        number1 = (EditText) v.findViewById(R.id.number1);
        number2 = (EditText) v.findViewById(R.id.number2);
        confirmButton = (Button) v.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);

        id = getArguments().getString("id");
        textView5.setText(id);
        if (id.equals("Date")){
            linearDate.setVisibility(View.VISIBLE);
            dTv.setText("day");
        }
        return  v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        boolean badEntry =  false;
        String solution[] = new String [2];
        Log.v("number1", number1.getText().toString());
        if (!number1.getText().toString().equals("")){
            solution[0] = number1.getText().toString();
            if (id.equals("Date")){
                if (!number2.getText().toString().equals("")){
                    solution[1] = number2.getText().toString();
                } else badEntry = true;
            }
        }
        else badEntry = true;
        if (badEntry) Toast.makeText(getActivity().getApplicationContext(), "Fill the gap before confirmation, please", Toast.LENGTH_SHORT).show();
        else {
            this.mListener.onComplete(solution);
            dismiss();
        }
    }


    public static interface OnCompleteListener {
        public abstract void onComplete(String[] res);
    }

    private OnCompleteListener mListener;

    // make sure the Activity implemented it
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
}
