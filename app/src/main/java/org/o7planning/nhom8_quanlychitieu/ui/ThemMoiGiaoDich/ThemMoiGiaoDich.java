package org.o7planning.nhom8_quanlychitieu.ui.ThemMoiGiaoDich;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.o7planning.nhom8_quanlychitieu.R;
import org.o7planning.nhom8_quanlychitieu.database.DatabaseHelper;
import org.o7planning.nhom8_quanlychitieu.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ThemMoiGiaoDich extends Fragment {

    private TextView dateTextView;
    private Spinner categorySpinner;
    private EditText amountEditText;
    private EditText titleEditText;
    private EditText noteEditText;
    private Button saveButton;
    private ImageView backButton;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private View dateContainer;
    private ImageView calendarIcon;

    // Thêm biến DatabaseHelper
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_them_moi_giao_dich, container, false);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        // Khởi tạo các thành phần
        initializeComponents(view);

        // Thiết lập ngày hiện tại
        setupDatePicker(view);

        // Thiết lập spinner danh mục
        setupCategorySpinner();

        // Thiết lập sự kiện click cho nút lưu
        setupSaveButton();

        // Thiết lập sự kiện click cho nút quay lại
        setupBackButton();

        return view;
    }

    private void initializeComponents(View view) {
        dateTextView = view.findViewById(R.id.date_text);
        categorySpinner = view.findViewById(R.id.category_spinner);
        amountEditText = view.findViewById(R.id.amount_edit_text);
        titleEditText = view.findViewById(R.id.title_edit_text);
        noteEditText = view.findViewById(R.id.note_edit_text);
        saveButton = view.findViewById(R.id.save_button);
        backButton = view.findViewById(R.id.back_button);
        dateContainer = view.findViewById(R.id.date_container);
        calendarIcon = view.findViewById(R.id.calendar_icon);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    private void setupDatePicker(View view) {
        // Hiển thị ngày hiện tại
        dateTextView.setText(dateFormat.format(calendar.getTime()));

        // Thiết lập sự kiện click cho ngày
        if (dateContainer != null) {
            dateContainer.setOnClickListener(v -> showDatePickerDialog());
        }

        // Thiết lập sự kiện click cho icon lịch
        if (calendarIcon != null) {
            calendarIcon.setOnClickListener(v -> showDatePickerDialog());
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dateTextView.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupCategorySpinner() {
        // Tạo danh sách các danh mục
        String[] categories = new String[]{
                "Chọn Danh Mục", "Thực Phẩm", "Giải Trí", "Mua Sắm", "Hóa Đơn", "Đi Lại", "Y Tế", "Giáo Dục", "Khác"
        };

        // Tạo adapter cho spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> {
            // Kiểm tra dữ liệu nhập vào
            if (validateInput()) {
                // Lưu giao dịch
                saveTransaction();

                // Hiển thị thông báo thành công
                Toast.makeText(getContext(), "Đã lưu giao dịch", Toast.LENGTH_SHORT).show();

                // Quay lại màn hình trước
                if (getActivity() != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        getActivity().getOnBackPressedDispatcher().onBackPressed();
                    } else {
                        getActivity().onBackPressed();
                    }
                }
            }
        });
    }

    private boolean validateInput() {
        // Kiểm tra danh mục
        if (categorySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra số tiền
        String amountStr = amountEditText.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra tiêu đề
        String title = titleEditText.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveTransaction() {
        // Lấy thông tin giao dịch
        String date = dateTextView.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        double amount = Double.parseDouble(amountEditText.getText().toString().replace("$", ""));
        String title = titleEditText.getText().toString();
        String note = noteEditText.getText().toString();

        // Tạo đối tượng Transaction
        Transaction transaction = new Transaction(date, category, amount, title, note);

        // Lưu giao dịch vào cơ sở dữ liệu
        long id = databaseHelper.addTransaction(transaction);

        if (id > 0) {
            Toast.makeText(getContext(), "Giao dịch đã được lưu thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Lỗi khi lưu giao dịch!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> {
            // Quay lại màn hình trước
            if (getActivity() != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                } else {
                    getActivity().onBackPressed();
                }
            }
        });
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
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                // Chuyển đến màn hình trang chủ
                // Ví dụ: ((MainActivity)getActivity()).navigateToHome();
            });
        }

        if (navHistory != null) {
            navHistory.setOnClickListener(v -> {
                // Chuyển đến màn hình lịch sử
                // Ví dụ: ((MainActivity)getActivity()).navigateToHistory();
            });
        }

        if (navAdd != null) {
            navAdd.setOnClickListener(v -> {
                // Đã ở màn hình thêm giao dịch
            });
        }

        if (navList != null) {
            navList.setOnClickListener(v -> {
                // Chuyển đến màn hình danh sách
                // Ví dụ: ((MainActivity)getActivity()).navigateToList();
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                // Chuyển đến màn hình hồ sơ
                // Ví dụ: ((MainActivity)getActivity()).navigateToProfile();
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}