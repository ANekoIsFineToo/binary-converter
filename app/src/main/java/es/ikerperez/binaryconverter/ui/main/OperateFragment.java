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
import es.ikerperez.binaryconverter.models.OperateResult;
import es.ikerperez.binaryconverter.utils.RootConverter;
import es.ikerperez.binaryconverter.utils.ViewUtil;
import timber.log.Timber;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 04/10/2016.
 */

public class OperateFragment extends Fragment {

    @BindView(R.id.main_operate_fragment) ScrollView mOperateFragment;
    @BindView(R.id.content) LinearLayout mContent;
    @BindView(R.id.first_value_container) TextInputLayout mFirstValueContainer;
    @BindView(R.id.first_value) TextInputEditText mFirstValue;
    @BindView(R.id.second_value_container) TextInputLayout mSecondValueContainer;
    @BindView(R.id.second_value) TextInputEditText mSecondValue;
    @BindView(R.id.value_base) Spinner mValueBase;
    @BindView(R.id.operation) Spinner mOperation;
    @BindView(R.id.calculate_operation) Button mCalculateOperation;
    @BindView(R.id.operation_results) RecyclerView mOperationResults;

    private OperateResultsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static OperateFragment newInstance() {
        return new OperateFragment();
    }

    public OperateFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_operate, container, false);

        ButterKnife.bind(this, rootView);

        mOperationResults.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext()) {

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mOperationResults.setLayoutManager(mLayoutManager);

        mAdapter = new OperateResultsAdapter(getContext());
        mOperationResults.setAdapter(mAdapter);

        mFirstValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFirstValueContainer.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mSecondValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSecondValueContainer.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mCalculateOperation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                singleCalculation();
            }
        });

        ViewUtil.setupHideKeyboard(getActivity(), mOperateFragment, mContent);

        return rootView;
    }

    private void singleCalculation() {
        Timber.i("Operation.");

        String firstValue = mFirstValue.getText().toString();
        String secondValue = mSecondValue.getText().toString();
        String base = mValueBase.getSelectedItem().toString();
        String operation = mOperation.getSelectedItem().toString();

        if (firstValue.isEmpty()) {
            Timber.i("Error first value is empty.");
            mFirstValueContainer.setError(getString(R.string.error_value_empty));
            mFirstValueContainer.setErrorEnabled(true);
            mFirstValue.requestFocus();
            ViewUtil.openKeyboard(getActivity());
            return;
        }

        if (secondValue.isEmpty()) {
            Timber.i("Error first value is empty.");
            mSecondValueContainer.setError(getString(R.string.error_value_empty));
            mSecondValueContainer.setErrorEnabled(true);
            mSecondValue.requestFocus();
            ViewUtil.openKeyboard(getActivity());
            return;
        }

        String parsedValue = RootConverter.operation(firstValue, secondValue, base, operation);

        if (parsedValue == null) {
            Timber.i("Error value is invalid.");
            Snackbar.make(mOperateFragment, getString(R.string.error_value_invalid),
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        mAdapter.add(new OperateResult(
                base, RootConverter.parseOrigin(firstValue, base),
                RootConverter.parseOrigin(secondValue, base), operation, parsedValue));
        mOperationResults.scrollToPosition(0);

        mFirstValue.setText(null);
        mSecondValue.setText(null);
    }
}
