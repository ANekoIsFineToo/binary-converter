package es.ikerperez.binaryconverter.ui.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.ikerperez.binaryconverter.R;
import es.ikerperez.binaryconverter.models.BinaryResult;
import es.ikerperez.binaryconverter.utils.RootConverter;
import es.ikerperez.binaryconverter.utils.ViewUtil;
import timber.log.Timber;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 01/10/2016.
 */

public class ConverterFragment extends Fragment {

    @BindView(R.id.main_converter_fragment) ScrollView mConverterFragment;
    @BindView(R.id.content) LinearLayout mContent;
    @BindView(R.id.binary_value_container) TextInputLayout mBinaryValueContainer;
    @BindView(R.id.binary_value) TextInputEditText mBinaryValue;
    @BindView(R.id.binary_origin) Spinner mBinaryOrigin;
    @BindView(R.id.binary_target) Spinner mBinaryTarget;
    @BindView(R.id.calculate_binary) Button mCalculateBinary;
    @BindView(R.id.binary_results) RecyclerView mBinaryResults;

    private BinaryResultsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static ConverterFragment newInstance() {
        return new ConverterFragment();
    }

    public ConverterFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_converter, container, false);

        ButterKnife.bind(this, rootView);

        mBinaryResults.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext()) {

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mBinaryResults.setLayoutManager(mLayoutManager);

        mAdapter = new BinaryResultsAdapter(getContext());
        mBinaryResults.setAdapter(mAdapter);

        mBinaryValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBinaryValueContainer.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mCalculateBinary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                singleCalculation();
            }
        });

        ViewUtil.setupHideKeyboard(getActivity(), mConverterFragment, mContent);

        return rootView;
    }

    private void singleCalculation() {
        Timber.i("Single calculation.");

        String value = mBinaryValue.getText().toString();
        String origin = mBinaryOrigin.getSelectedItem().toString();
        String target = getResources()
                .getStringArray(R.array.binary_short)[mBinaryTarget.getSelectedItemPosition()];

        if (value.isEmpty()) {
            Timber.i("Error value is empty.");
            mBinaryValueContainer.setError(getString(R.string.error_value_empty));
            mBinaryValueContainer.setErrorEnabled(true);
            mBinaryValue.requestFocus();
            ViewUtil.openKeyboard(getActivity());
            return;
        }

        if (origin.equals(target)) {
            Timber.i("Error origin and target are equals.");
            Snackbar.make(mConverterFragment, getString(R.string.error_origin_target_same),
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        String parsedValue = RootConverter.parseNumber(value, origin, target);

        if (parsedValue == null) {
            Timber.i("Error value is invalid.");
            mBinaryValueContainer.setError(getString(R.string.error_invalid_value));
            mBinaryValueContainer.setErrorEnabled(true);
            mBinaryValue.requestFocus();
            ViewUtil.openKeyboard(getActivity());
            return;
        }

        mAdapter.add(new BinaryResult(
                RootConverter.parseOrigin(value, origin), parsedValue, origin, target));
        mBinaryResults.scrollToPosition(0);

        mBinaryValue.setText(null);
    }
}
