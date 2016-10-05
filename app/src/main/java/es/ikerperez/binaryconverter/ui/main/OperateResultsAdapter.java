package es.ikerperez.binaryconverter.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.ikerperez.binaryconverter.R;
import es.ikerperez.binaryconverter.models.OperateResult;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 05/10/2016.
 */

public class OperateResultsAdapter extends RecyclerView.Adapter<OperateResultsAdapter.OperateViewHolder> {

    private List<OperateResult> mItems;
    private Context mContext;
    private boolean triggerAnimation = false;

    public static class OperateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_operation_result) RelativeLayout mRelativeLayout;
        @BindView(R.id.result_title) TextView mResultTitle;
        @BindView(R.id.first_value) TextView mFirstValue;
        @BindView(R.id.second_value) TextView mSecondValue;
        @BindView(R.id.result_value) TextView mResultValue;

        public OperateViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public OperateResultsAdapter(Context context) {
        this.mItems = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public OperateResultsAdapter.OperateViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_operation_result, parent, false);

        return new OperateResultsAdapter.OperateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OperateResultsAdapter.OperateViewHolder holder, int position) {
        OperateResult operateResult = mItems.get(position);

        String operation = null;
        String base = mContext.getResources()
                .getStringArray(R.array.base_long)[Integer.parseInt(operateResult.getBase()) - 2];

        switch (operateResult.getOperation()) {
            case "+":
                operation = mContext.getString(R.string.main_plus);
                break;
            case "-":
                operation = mContext.getString(R.string.main_substract);
                break;
            case "*":
                operation = mContext.getString(R.string.main_multiply);
                break;
            case "/":
                operation = mContext.getString(R.string.main_divide);
                break;
            case "^":
                operation = mContext.getString(R.string.main_pow);
        }

        holder.mResultTitle.setText(
                String.format(mContext.getString(R.string.operation_base), operation, base));
        holder.mFirstValue.setText(
                String.format(mContext.getString(R.string.binary_to_result),
                        mContext.getString(R.string.main_first_value),
                        operateResult.getFirstValue().toUpperCase()));
        holder.mSecondValue.setText(
                String.format(mContext.getString(R.string.binary_to_result),
                        mContext.getString(R.string.main_second_value),
                        operateResult.getSecondValue().toUpperCase()));
        holder.mResultValue.setText(
                String.format(mContext.getString(R.string.binary_to_result),
                        mContext.getString(R.string.main_result),
                        operateResult.getResult().toUpperCase()));

        setAnimation(holder.mRelativeLayout, position);
    }

    public void add(OperateResult operateResult) {
        if (mItems.size() > 0) {
            OperateResult preOperateResult = mItems.get(0);

            if (operateResult.compareTo(preOperateResult)) {
                return;
            }
        }

        mItems.add(0, operateResult);
        notifyItemInserted(0);

        triggerAnimation = true;
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position == 0 && triggerAnimation) {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);

            triggerAnimation = false;
        }
    }
}
