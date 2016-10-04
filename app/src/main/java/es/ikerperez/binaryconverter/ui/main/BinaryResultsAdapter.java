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
import es.ikerperez.binaryconverter.models.BinaryResult;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 01/10/2016.
 */

public class BinaryResultsAdapter extends
        RecyclerView.Adapter<BinaryResultsAdapter.BinaryViewHolder> {

    private List<BinaryResult> mItems;
    private Context mContext;
    private boolean triggerAnimation = false;

    public static class BinaryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_binary_result) RelativeLayout mRelativeLayout;
        @BindView(R.id.result_title) TextView mResultTitle;
        @BindView(R.id.result_previous) TextView mResultPrevious;
        @BindView(R.id.result_after) TextView mResultAfter;

        public BinaryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public BinaryResultsAdapter(Context context) {
        this.mItems = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public BinaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_binary_result, parent, false);

        return new BinaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BinaryViewHolder holder, int position) {
        BinaryResult binaryResult = mItems.get(position);

        String origin = mContext.getString(mContext.getResources().getIdentifier(
                "binary_short_to_long_".concat(binaryResult.getOrigin()),
                "string", mContext.getPackageName()));

        String target = mContext.getString(mContext.getResources().getIdentifier(
                "binary_short_to_long_".concat(binaryResult.getTarget()),
                "string", mContext.getPackageName()));

        holder.mResultTitle.setText(
                String.format(mContext.getString(R.string.binary_to_binary), origin, target));
        holder.mResultPrevious.setText(
                String.format(mContext.getString(R.string.binary_to_result), origin,
                        binaryResult.getPrev().toUpperCase()));
        holder.mResultAfter.setText(
                String.format(mContext.getString(R.string.binary_to_result), target,
                        binaryResult.getValue().toUpperCase()));

        setAnimation(holder.mRelativeLayout, position);
    }

    public void add(BinaryResult binaryResult) {
        if (mItems.size() > 0) {
            BinaryResult preBinaryResult = mItems.get(0);

            if (binaryResult.compareTo(preBinaryResult)) {
                return;
            }
        }

        mItems.add(0, binaryResult);
        notifyItemInserted(0);

        triggerAnimation = true;
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position == 0 && triggerAnimation) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);

            triggerAnimation = false;
        }
    }
}
