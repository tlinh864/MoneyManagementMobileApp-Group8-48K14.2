package org.o7planning.nhom8_quanlychitieu.ui.PhanTich;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.o7planning.nhom8_quanlychitieu.R;
import org.o7planning.nhom8_quanlychitieu.database.DatabaseHelper;
import org.o7planning.nhom8_quanlychitieu.model.Transaction;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PhanTich extends Fragment {

    private PieChart pieChart;
    private TextView monthIncomeTextView;
    private Button incomeButton, expenseButton;
    private ImageView backButton;
    private DatabaseHelper databaseHelper;
    private String currentMonth = "4/2025"; // Mặc định là tháng 4/2025
    private boolean showingIncome = true; // Mặc định hiển thị thu nhập

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nhận tháng từ arguments của Navigation Component
        if (getArguments() != null) {
            currentMonth = getArguments().getString("month", "4/2025");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phan_tich, container, false);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        // Khởi tạo các thành phần
        initializeComponents(view);

        // Thiết lập sự kiện click cho các nút
        setupButtonListeners();

        // Thiết lập dữ liệu ban đầu (thu nhập)
        updateChartData(true);

        // Thiết lập bottom navigation
        setupBottomNavigation(view);

        return view;
    }

    private void initializeComponents(View view) {
        pieChart = view.findViewById(R.id.pieChart);
        monthIncomeTextView = view.findViewById(R.id.month_income_text);
        incomeButton = view.findViewById(R.id.income_button);
        expenseButton = view.findViewById(R.id.expense_button);
        backButton = view.findViewById(R.id.back_button);

        // Thiết lập PieChart
        setupPieChart();
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(24f);
        pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false); // Tắt legend mặc định vì chúng ta sẽ tự tạo legend
    }

    private void setupButtonListeners() {
        // Nút Thu Nhập
        incomeButton.setOnClickListener(v -> {
            if (!showingIncome) {
                showingIncome = true;
                updateButtonStyles();
                updateChartData(true);
            }
        });

        // Nút Chi Tiêu
        expenseButton.setOnClickListener(v -> {
            if (showingIncome) {
                showingIncome = false;
                updateButtonStyles();
                updateChartData(false);
            }
        });

        // Nút Quay lại - Sử dụng OnBackPressedDispatcher thay vì onBackPressed()
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void updateButtonStyles() {
        if (showingIncome) {
            incomeButton.setBackgroundResource(R.drawable.button_selected_bg);
            expenseButton.setBackgroundResource(R.drawable.button_unselected_bg);
        } else {
            incomeButton.setBackgroundResource(R.drawable.button_unselected_bg);
            expenseButton.setBackgroundResource(R.drawable.button_selected_bg);
        }
    }

    private void updateChartData(boolean isIncome) {
        // Lấy dữ liệu từ database
        List<Transaction> transactions = getTransactionsForMonth(currentMonth, isIncome);

        // Tính tổng thu nhập/chi tiêu
        double total = calculateTotal(transactions);

        // Cập nhật text hiển thị
        String formattedTotal = NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(total).replace("$", "$");

        if (isIncome) {
            monthIncomeTextView.setText("Thu Nhập Tháng " + currentMonth + ": " + formattedTotal);
        } else {
            monthIncomeTextView.setText("Chi Tiêu Tháng " + currentMonth + ": " + formattedTotal);
        }

        // Tạo dữ liệu cho biểu đồ
        ArrayList<PieEntry> entries = new ArrayList<>();
        Map<String, Double> categoryTotals = calculateCategoryTotals(transactions);

        // Thêm dữ liệu vào biểu đồ
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            float percentage = (float) (entry.getValue() / total * 100);
            entries.add(new PieEntry(percentage, entry.getKey()));
        }

        // Tạo dataset
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(getChartColors());
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new PercentFormatter(pieChart));

        // Tạo PieData
        PieData data = new PieData(dataSet);

        // Cập nhật biểu đồ
        pieChart.setData(data);
        pieChart.invalidate();

        // Cập nhật legend tự tạo
        updateCustomLegend(categoryTotals, getChartColors());
    }

    private List<Transaction> getTransactionsForMonth(String month, boolean isIncome) {
        // Giả lập dữ liệu cho mục đích demo
        // Trong thực tế, bạn sẽ lấy dữ liệu từ database
        List<Transaction> transactions = new ArrayList<>();

        if (isIncome) {
            transactions.add(new Transaction(1, "15/4/2025", "Lương", 5700, "Lương tháng 4", "", false));
            transactions.add(new Transaction(2, "15/4/2025", "Phụ Cấp", 1256.4, "Phụ cấp ăn trưa", "", false));
            transactions.add(new Transaction(3, "20/4/2025", "Đầu Tư", 799.6, "Cổ tức", "", false));
            transactions.add(new Transaction(4, "25/4/2025", "Partime", 1940, "Dạy kèm", "", false));
            transactions.add(new Transaction(5, "28/4/2025", "Lương", 1724, "Thưởng dự án", "", false));
        } else {
            transactions.add(new Transaction(6, "05/4/2025", "Thực Phẩm", 1200, "Đi chợ", "", true));
            transactions.add(new Transaction(7, "10/4/2025", "Giải Trí", 800, "Xem phim", "", true));
            transactions.add(new Transaction(8, "15/4/2025", "Hóa Đơn", 1500, "Tiền điện nước", "", true));
            transactions.add(new Transaction(9, "20/4/2025", "Di Chuyển", 500, "Xăng xe", "", true));
            transactions.add(new Transaction(10, "25/4/2025", "Mua Sắm", 3000, "Quần áo", "", true));
        }

        return transactions;
    }

    private double calculateTotal(List<Transaction> transactions) {
        double total = 0;
        for (Transaction transaction : transactions) {
            total += transaction.getAmount();
        }
        return total;
    }

    private Map<String, Double> calculateCategoryTotals(List<Transaction> transactions) {
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Transaction transaction : transactions) {
            String category = transaction.getCategory();
            double amount = transaction.getAmount();

            if (categoryTotals.containsKey(category)) {
                categoryTotals.put(category, categoryTotals.get(category) + amount);
            } else {
                categoryTotals.put(category, amount);
            }
        }

        return categoryTotals;
    }

    private int[] getChartColors() {
        return new int[]{
                Color.parseColor("#1ABC9C"), // Xanh lá
                Color.parseColor("#3498DB"), // Xanh dương
                Color.parseColor("#9B59B6"), // Tím
                Color.parseColor("#E67E22"), // Cam
                Color.parseColor("#F1C40F")  // Vàng
        };
    }

    private void updateCustomLegend(Map<String, Double> categoryTotals, int[] colors) {
        // Cập nhật các view legend tự tạo
        // Trong thực tế, bạn sẽ tạo các view này động dựa trên số lượng danh mục

        // Ví dụ: Cập nhật màu và text cho các legend item
        View legendItem1 = getView().findViewById(R.id.legend_item_1);
        View legendItem2 = getView().findViewById(R.id.legend_item_2);
        View legendItem3 = getView().findViewById(R.id.legend_item_3);
        View legendItem4 = getView().findViewById(R.id.legend_item_4);

        TextView legendText1 = getView().findViewById(R.id.legend_text_1);
        TextView legendText2 = getView().findViewById(R.id.legend_text_2);
        TextView legendText3 = getView().findViewById(R.id.legend_text_3);
        TextView legendText4 = getView().findViewById(R.id.legend_text_4);

        View legendColor1 = getView().findViewById(R.id.legend_color_1);
        View legendColor2 = getView().findViewById(R.id.legend_color_2);
        View legendColor3 = getView().findViewById(R.id.legend_color_3);
        View legendColor4 = getView().findViewById(R.id.legend_color_4);

        // Ẩn tất cả legend items trước
        legendItem1.setVisibility(View.GONE);
        legendItem2.setVisibility(View.GONE);
        legendItem3.setVisibility(View.GONE);
        legendItem4.setVisibility(View.GONE);

        // Hiển thị và cập nhật các legend items dựa trên dữ liệu
        int i = 0;
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            if (i >= 4) break; // Chỉ hiển thị tối đa 4 legend items

            String category = entry.getKey();
            int color = colors[i % colors.length];

            switch (i) {
                case 0:
                    legendItem1.setVisibility(View.VISIBLE);
                    legendText1.setText(category);
                    legendColor1.setBackgroundColor(color);
                    break;
                case 1:
                    legendItem2.setVisibility(View.VISIBLE);
                    legendText2.setText(category);
                    legendColor2.setBackgroundColor(color);
                    break;
                case 2:
                    legendItem3.setVisibility(View.VISIBLE);
                    legendText3.setText(category);
                    legendColor3.setBackgroundColor(color);
                    break;
                case 3:
                    legendItem4.setVisibility(View.VISIBLE);
                    legendText4.setText(category);
                    legendColor4.setBackgroundColor(color);
                    break;
            }

            i++;
        }
    }

    private void setupBottomNavigation(View view) {
        // Tìm các nút điều hướng
        View navHome = view.findViewById(R.id.nav_home);
        View navHistory = view.findViewById(R.id.nav_history);
        View navAdd = view.findViewById(R.id.nav_add);
        View navList = view.findViewById(R.id.nav_list);
        View navProfile = view.findViewById(R.id.nav_profile);

        // Thiết lập sự kiện click
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                // Chuyển đến màn hình trang chủ
                if (getActivity() != null) {
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            });
        }

        if (navHistory != null) {
            navHistory.setOnClickListener(v -> {
                // Chuyển đến màn hình lịch sử
                // Ví dụ: Navigation.findNavController(view).navigate(R.id.action_phanTich_to_giaodich);
            });
        }

        if (navAdd != null) {
            navAdd.setOnClickListener(v -> {
                // Chuyển đến màn hình thêm giao dịch
                // Ví dụ: Navigation.findNavController(view).navigate(R.id.action_phanTich_to_themmoigiaodich);
            });
        }

        if (navList != null) {
            navList.setOnClickListener(v -> {
                // Chuyển đến màn hình danh sách
                // Ví dụ: Navigation.findNavController(view).navigate(R.id.action_phanTich_to_danhmuc);
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                // Chuyển đến màn hình hồ sơ
                // Ví dụ: Navigation.findNavController(view).navigate(R.id.action_phanTich_to_hoso);
            });
        }
    }

    // Phương thức để tạo instance mới với tháng được chỉ định
    public static PhanTich newInstance(String month) {
        PhanTich fragment = new PhanTich();
        Bundle args = new Bundle();
        args.putString("month", month);
        fragment.setArguments(args);
        return fragment;
    }
}