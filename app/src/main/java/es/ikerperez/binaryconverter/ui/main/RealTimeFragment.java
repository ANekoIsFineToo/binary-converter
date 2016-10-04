package es.ikerperez.binaryconverter.ui.main;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.ikerperez.binaryconverter.R;
import es.ikerperez.binaryconverter.utils.RootConverter;
import es.ikerperez.binaryconverter.utils.ViewUtil;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 01/10/2016.
 */

public class RealTimeFragment extends Fragment {

    @BindView(R.id.main_real_time_fragment) ScrollView mRealTimeFragment;
    @BindView(R.id.content) LinearLayout mContent;
    @BindView(R.id.binary_value_container) TextInputLayout mBinaryValueContainer;
    @BindView(R.id.binary_value) TextInputEditText mBinaryValue;
    @BindView(R.id.octal_value_container) TextInputLayout mOctalValueContainer;
    @BindView(R.id.octal_value) TextInputEditText mOctalValue;
    @BindView(R.id.decimal_value_container) TextInputLayout mDecimalValueContainer;
    @BindView(R.id.decimal_value) TextInputEditText mDecimalValue;
    @BindView(R.id.hex_value_container) TextInputLayout mHexValueContainer;
    @BindView(R.id.hex_value) TextInputEditText mHexValue;
    @BindView(R.id.empty_all) Button mEmptyAll;

    private TextInputLayout mActiveTextInputLayout;

    public static RealTimeFragment newInstance() {
        return new RealTimeFragment();
    }

    public RealTimeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_real_time, container, false);

        ButterKnife.bind(this, rootView);

        loadTextWatchers();

        mEmptyAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                emptyAll();
            }
        });

        ViewUtil.setupHideKeyboard(getActivity(), mRealTimeFragment, mContent);

        return rootView;
    }

    private void loadTextWatchers() {
        mBinaryValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = String.valueOf(s);

                if (mBinaryValue.hasFocus()) {
                    mActiveTextInputLayout = mBinaryValueContainer;

                    if (!number.isEmpty()) {
                        setValues("active",
                                RootConverter.parseNumber(number, "2", "8"),
                                RootConverter.parseNumber(number, "2", "10"),
                                RootConverter.parseNumber(number, "2", "16"));
                    } else {
                        emptyAll();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mOctalValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = String.valueOf(s);

                if (mOctalValue.hasFocus()) {
                    mActiveTextInputLayout = mOctalValueContainer;

                    if (!number.isEmpty()) {
                        setValues(RootConverter.parseNumber(number, "8", "2"),
                                "active",
                                RootConverter.parseNumber(number, "8", "10"),
                                RootConverter.parseNumber(number, "8", "16"));
                    } else {
                        emptyAll();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mDecimalValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = String.valueOf(s);

                if (mDecimalValue.hasFocus()) {
                    mActiveTextInputLayout = mDecimalValueContainer;

                    if (!number.isEmpty()) {
                        setValues(RootConverter.parseNumber(number, "10", "2"),
                                RootConverter.parseNumber(number, "10", "8"),
                                "active",
                                RootConverter.parseNumber(number, "10", "16"));
                    } else {
                        emptyAll();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mHexValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = String.valueOf(s);

                if (mHexValue.hasFocus()) {
                    mActiveTextInputLayout = mHexValueContainer;

                    if (!number.isEmpty()) {
                        setValues(RootConverter.parseNumber(number, "16", "2"),
                                RootConverter.parseNumber(number, "16", "8"),
                                RootConverter.parseNumber(number, "16", "10"),
                                "active");
                    } else {
                        emptyAll();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void setValues(String binary, String octal, String decimal, String hex) {
        clearErrors();

        if (binary == null || octal == null || decimal == null || hex == null) {
            mActiveTextInputLayout.setError(getString(R.string.error_invalid_value));
            mActiveTextInputLayout.setErrorEnabled(true);

            return;
        }

        if (!binary.equals("active")) {
            mBinaryValue.setText(binary);
        }

        if (!octal.equals("active")) {
            mOctalValue.setText(octal);
        }

        if (!decimal.equals("active")) {
            mDecimalValue.setText(decimal);
        }

        if (!hex.equals("active")) {
            mHexValue.setText(hex.toUpperCase());
        }
    }

    private void emptyAll() {
        if (!mBinaryValue.getText().toString().isEmpty()) {
            mBinaryValue.setText(null);
        }

        if (!mOctalValue.getText().toString().isEmpty()) {
            mOctalValue.setText(null);
        }

        if (!mDecimalValue.getText().toString().isEmpty()) {
            mDecimalValue.setText(null);
        }

        if (!mHexValue.getText().toString().isEmpty()) {
            mHexValue.setText(null);
        }
    }

    private void clearErrors() {
        mBinaryValueContainer.setErrorEnabled(false);
        mOctalValueContainer.setErrorEnabled(false);
        mDecimalValueContainer.setErrorEnabled(false);
        mHexValueContainer.setErrorEnabled(false);
    }
}
