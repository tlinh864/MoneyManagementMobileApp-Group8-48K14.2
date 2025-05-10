package org.o7planning.nhom8_quanlychitieu.ui.TrangChu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.o7planning.nhom8_quanlychitieu.R;

public class TrangChu extends Fragment {

    private static final String TAG = "TrangChu";
    private TextView totalBalance;
    private TextView totalExpense;
    private LinearLayout transactionContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        // Initialize views
        totalBalance = view.findViewById(R.id.total_balance);
        totalExpense = view.findViewById(R.id.total_expense);
        transactionContainer = view.findViewById(R.id.transaction_container);

        // Set initial data
        totalBalance.setText("$7,783.00");
        totalExpense.setText("-$1,187.40");

        // Thiết lập sự kiện click cho phần doanh thu tháng
        setupMonthIncomeClickListener(view);

        return view;
    }

    private void setupMonthIncomeClickListener(View view) {
        // Tìm view chứa thông tin doanh thu tháng
        View monthIncomeContainer = view.findViewById(R.id.month_income_container);

        if (monthIncomeContainer != null) {
            monthIncomeContainer.setOnClickListener(v -> {
                try {
                    // Chuyển đến màn hình phân tích sử dụng Navigation Component
                    Bundle args = new Bundle();
                    args.putString("month", "4/2025");

                    // Kiểm tra xem action có tồn tại không
                    if (Navigation.findNavController(view).getCurrentDestination().getAction(R.id.action_trangchu_to_phanTich) != null) {
                        Navigation.findNavController(view).navigate(R.id.action_trangchu_to_phanTich, args);
                    } else {
                        // Nếu action không tồn tại, chuyển trực tiếp đến destination
                        Navigation.findNavController(view).navigate(R.id.phanTich, args);
                        Log.d(TAG, "Navigating directly to phanTich destination");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Navigation error: " + e.getMessage());
                    Toast.makeText(getContext(), "Không thể chuyển đến màn hình phân tích: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Log lỗi nếu không tìm thấy view
            Log.e(TAG, "ERROR: Không tìm thấy view với ID month_income_container");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Thiết lập các sự kiện click cho bottom navigation
        setupBottomNavigation(view);
    }

    private void setupBottomNavigation(View view) {
        // Tìm các nút điều hướng
        View navHome = view.findViewById(R.id.nav_home);
        View navHistory = view.findViewById(R.id.nav_history);
        View navAdd = view.findViewById(R.id.nav_add);
        View navList = view.findViewById(R.id.nav_list);
        View navProfile = view.findViewById(R.id.nav_profile);

        // Thiết lập sự kiện click
        navHome.setOnClickListener(v -> {
            // Đã ở trang chủ
        });

        navHistory.setOnClickListener(v -> {
            // Chuyển đến màn hình lịch sử
            // Ví dụ: ((MainActivity)getActivity()).navigateToHistory();
        });

        navAdd.setOnClickListener(v -> {
            // Mở dialog thêm giao dịch mới
            // Ví dụ: showAddTransactionDialog();
        });

        navList.setOnClickListener(v -> {
            // Chuyển đến màn hình danh sách
            // Ví dụ: ((MainActivity)getActivity()).navigateToList();
        });

        navProfile.setOnClickListener(v -> {
            // Chuyển đến màn hình hồ sơ
            // Ví dụ: ((MainActivity)getActivity()).navigateToProfile();
        });
    }
}