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
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 04/10/2016.
 */

public class BaseNFragment extends Fragment {

    @BindView(R.id.main_base_n_fragment) ScrollView mBaseNFragment;
    @BindView(R.id.content) LinearLayout mContent;
    @BindView(R.id.base_n_value_container) TextInputLayout mBaseNValueContainer;
    @BindView(R.id.base_n_value) TextInputEditText mBaseNValue;
    @BindView(R.id.base_n_origin) Spinner mBaseNOrigin;
    @BindView(R.id.base_n_target) Spinner mBaseNTarget;
    @BindView(R.id.calculate_base_n) Button mCalculateBaseN;
    @BindView(R.id.base_n_results) RecyclerView mBaseNResults;

    private BaseNResultsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static BaseNFragment newInstance() {
        return new BaseNFragment();
    }

    public BaseNFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_base_n, container, false);

        ButterKnife.bind(this, rootView);

        mBaseNResults.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext()) {

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mBaseNResults.setLayoutManager(mLayoutManager);

        mAdapter = new BaseNResultsAdapter(getContext());
        mBaseNResults.setAdapter(mAdapter);

        mBaseNValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBaseNValueContainer.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mCalculateBaseN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                singleCalculation();
            }
        });

        ViewUtil.setupHideKeyboard(getActivity(), mBaseNFragment, mContent);

        return rootView;
    }

    private void singleCalculation() {
        Timber.i("Single Base N calculation.");

        String value = mBaseNValue.getText().toString();
        String origin = mBaseNOrigin.getSelectedItem().toString();
        String target = getResources()
                .getStringArray(R.array.base_short)[mBaseNTarget.getSelectedItemPosition()];

        if (value.isEmpty()) {
            Timber.i("Error value is empty.");
            mBaseNValueContainer.setError(getString(R.string.error_value_empty));
            mBaseNValueContainer.setErrorEnabled(true);
            mBaseNValue.requestFocus();
            ViewUtil.openKeyboard(getActivity());
            return;
        }

        if (origin.equals(target)) {
            Timber.i("Error origin and target are equals.");
            Snackbar.make(mBaseNFragment, getString(R.string.error_origin_target_same),
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        String parsedValue = RootConverter.parseNumber(value, origin, target);

        if (parsedValue == null) {
            Timber.i("Error value is invalid.");
            mBaseNValueContainer.setError(getString(R.string.error_invalid_value));
            mBaseNValueContainer.setErrorEnabled(true);
            mBaseNValue.requestFocus();
            ViewUtil.openKeyboard(getActivity());
            return;
        }

        mAdapter.add(new BinaryResult(
                RootConverter.parseOrigin(value, origin), parsedValue, origin, target));
        mBaseNResults.scrollToPosition(0);

        mBaseNValue.setText(null);
    }
}
