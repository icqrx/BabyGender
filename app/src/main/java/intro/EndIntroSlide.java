package intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marknguyen.babygenderpredictor.R;


public final class EndIntroSlide extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
//            text = savedInstanceState.getString(DATA_TEXT);
//            isCheckboxChecked = savedInstanceState.getBoolean(DATA_CHECKBOX);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString(DATA_TEXT, editText.getText().toString());
//        outState.putBoolean(DATA_CHECKBOX, checkBox.isChecked());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro3, container, false);

//        editText = (AppCompatEditText)view.findViewById(R.id.slide_input_edittext);
//        checkBox = (AppCompatCheckBox) view.findViewById(R.id.slide_input_checkbox);

        return view;
    }


}
