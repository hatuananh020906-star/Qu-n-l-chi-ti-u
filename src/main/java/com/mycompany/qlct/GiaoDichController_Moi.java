package com.mycompany.qlct;

import java.awt.event.ActionEvent;
import java.time.YearMonth;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GiaoDichController_Moi {

    private PanelSoGiaoDich pnlSo;
    private PanelThemGiaoDich pnlThem;
    private DialogSuaGiaoDich dialogSua;
    private GiaoDichDAO dao;
    private String userId;
    private String maGDDangChon = ""; // Lưu lại mã để biết đường đường Sửa/Xóa

    public GiaoDichController_Moi(PanelSoGiaoDich pnlSo, PanelThemGiaoDich pnlThem, DialogSuaGiaoDich dialogSua, String userId) {
        this.pnlSo = pnlSo;
        this.pnlThem = pnlThem;
        this.dialogSua = dialogSua;
        this.dao = new GiaoDichDAO();
        this.userId = userId;

        initEvents();
        loadDanhMucToComboBox();
        loadDuLieuTheoThang();
    }

    public void loadDanhMucToComboBox() {
        try {
            List<String> listDM = dao.getDanhSachTenDanhMuc();

            // DÒNG THẦN THÁNH ĐỂ KIỂM TRA: Xem ở ô Output phía dưới NetBeans có hiện dòng này không
            System.out.println("===> NHẬT KÝ KIỂM TRA: Đã lấy được " + (listDM != null ? listDM.size() : 0) + " danh mục từ CSDL.");

            if (pnlThem.getCbDanhMucThu() != null) {
                pnlThem.getCbDanhMucThu().removeAllItems();
                for (String tenDM : listDM) {
                    pnlThem.getCbDanhMucThu().addItem(tenDM);
                }
            }
            if (pnlThem.getCbDanhMucChi() != null) {
                pnlThem.getCbDanhMucChi().removeAllItems();
                for (String tenDM : listDM) {
                    pnlThem.getCbDanhMucChi().addItem(tenDM);
                }
            }

            pnlThem.getCbDanhMucThu().updateUI();
            pnlThem.getCbDanhMucChi().updateUI();

        } catch (Exception e) {
            System.out.println("===> 🚨 LỖI SẬP LUỒNG TẠI CONTROLLER: ");
            e.printStackTrace();
        }
    }

    private void initEvents() {
        // ================= KÍCH HOẠT TÌM KIẾM THỜI GIAN THỰC =================
        if (pnlSo.getTxtTimKiem() != null) {
            // Xóa bộ lắng nghe cũ nếu có để tránh lặp
            for (java.awt.event.KeyListener kl : pnlSo.getTxtTimKiem().getKeyListeners()) {
                pnlSo.getTxtTimKiem().removeKeyListener(kl);
            }

            // Gán sự kiện gõ chữ
            pnlSo.getTxtTimKiem().addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    String tuKhoa = pnlSo.getTxtTimKiem().getText().trim();
                    YearMonth ym = pnlSo.getThangHienTai();

                    if (tuKhoa.isEmpty()) {
                        loadDuLieuTheoThang(); // Nếu ô trống, hiện lại toàn bộ tháng đó
                    } else {
                        // Gọi hàm tìm kiếm từ DAO
                        List<Object[]> listKetQua = dao.timKiemGiaoDich(userId, ym.getMonthValue(), ym.getYear(), tuKhoa);
                        pnlSo.renderDanhSachGiaoDich(listKetQua); // Vẽ lại danh sách đã lọc
                    }
                }
            });
        }
        // =====================================================================
        // ================= KÍCH HOẠT TÌM KIẾM THỜI GIAN THỰC =================
        if (pnlSo.getTxtTimKiem() != null) {
            // Xóa bộ lắng nghe cũ nếu có để tránh lặp
            for (java.awt.event.KeyListener kl : pnlSo.getTxtTimKiem().getKeyListeners()) {
                pnlSo.getTxtTimKiem().removeKeyListener(kl);
            }

            // Gán sự kiện gõ chữ
            pnlSo.getTxtTimKiem().addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    String tuKhoa = pnlSo.getTxtTimKiem().getText().trim();
                    YearMonth ym = pnlSo.getThangHienTai();

                    if (tuKhoa.isEmpty()) {
                        loadDuLieuTheoThang(); // Nếu ô trống, hiện lại toàn bộ tháng đó
                    } else {
                        // Gọi hàm tìm kiếm từ DAO
                        List<Object[]> listKetQua = dao.timKiemGiaoDich(userId, ym.getMonthValue(), ym.getYear(), tuKhoa);
                        pnlSo.renderDanhSachGiaoDich(listKetQua); // Vẽ lại danh sách đã lọc
                    }
                }
            });
        }
        // =====================================================================
        // 1. Chuyển tháng trên Sổ giao dịch
        pnlSo.getBtnLui().addActionListener((ActionEvent e) -> loadDuLieuTheoThang());
        pnlSo.getBtnTien().addActionListener((ActionEvent e) -> loadDuLieuTheoThang());

        // 2. Click chọn 1 giao dịch để sửa/xóa
        pnlSo.setOnGiaoDichClickListener((Object[] rowData) -> {
            maGDDangChon = rowData[0].toString();

            // Ép nạp danh mục thật vào Dialog Sửa trước khi hiển thị (Sửa lỗi Item 1, 2, 3)
            if (dialogSua.getCbDanhMuc() != null) {
                dialogSua.getCbDanhMuc().removeAllItems();
                List<String> listDM = dao.getDanhSachTenDanhMuc();
                for (String tenDM : listDM) {
                    dialogSua.getCbDanhMuc().addItem(tenDM);
                }
            }

            dialogSua.getTxtSoTien().setText(String.format("%.0f", (Double) rowData[1]));
            dialogSua.getCbDanhMuc().setSelectedItem(rowData[3].toString());
            dialogSua.setLocationRelativeTo(pnlSo);
            dialogSua.setVisible(true);
        });

        // 3. Bấm nút Hủy trên bảng thêm -> Thu bảng xuống đáy
        pnlThem.getBtnHuy().addActionListener(e -> thuBangThem());

        // ================= BIỆN PHÁP CHỐNG LẶP SỰ KIỆN NÚT LƯU =================
        // 4. Nút LƯU KHOẢN THU (Xóa sạch các bộ lắng nghe cũ trước khi add mới)
        if (pnlThem.getBtnLuuThu() != null) {
            for (java.awt.event.ActionListener al : pnlThem.getBtnLuuThu().getActionListeners()) {
                pnlThem.getBtnLuuThu().removeActionListener(al);
            }
            pnlThem.getBtnLuuThu().addActionListener(e -> handleLuuThu());
        }

        // 5. Nút LƯU KHOẢN CHI (Xóa sạch các bộ lắng nghe cũ trước khi add mới)
        if (pnlThem.getBtnLuuChi() != null) {
            for (java.awt.event.ActionListener al : pnlThem.getBtnLuuChi().getActionListeners()) {
                pnlThem.getBtnLuuChi().removeActionListener(al);
            }
            pnlThem.getBtnLuuChi().addActionListener(e -> handleLuuChi());
        }

        // =======================================================================
        // 6. Bấm nút CẬP NHẬT trên Dialog sửa
        dialogSua.getBtnCapNhat().addActionListener(e -> {
            try {
                double soTien = Double.parseDouble(dialogSua.getTxtSoTien().getText().trim());
                String danhMuc = dialogSua.getCbDanhMuc().getSelectedItem().toString();
                if (dao.updateGiaoDich(maGDDangChon, soTien, danhMuc)) {
                    JOptionPane.showMessageDialog(dialogSua, "Cập nhật thành công!");
                    dialogSua.dispose();
                    loadDuLieuTheoThang();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogSua, "Dữ liệu sửa không hợp lệ!");
            }
        });

        // 7. Bấm nút XÓA trên Dialog sửa
        dialogSua.getBtnXoa().addActionListener(e -> {
            int chon = JOptionPane.showConfirmDialog(dialogSua, "Bạn có chắc chắn muốn xóa giao dịch này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (chon == JOptionPane.YES_OPTION) {
                if (dao.deleteGiaoDich(maGDDangChon)) {
                    JOptionPane.showMessageDialog(dialogSua, "Xóa giao dịch thành công!");
                    dialogSua.dispose();
                    loadDuLieuTheoThang();
                }
            }
        });

        dialogSua.getBtnThoat().addActionListener(e -> dialogSua.dispose());
        // ================= KÍCH HOẠT TÌM KIẾM THỜI GIAN THỰC =================
        if (pnlSo.getTxtTimKiem() != null) {
            // Xóa toàn bộ các bộ lắng nghe cũ để tránh trùng lặp
            for (java.awt.event.KeyListener kl : pnlSo.getTxtTimKiem().getKeyListeners()) {
                pnlSo.getTxtTimKiem().removeKeyListener(kl);
            }

            // Bắt sự kiện nhả phím (gõ chữ đến đâu lọc đến đấy)
            pnlSo.getTxtTimKiem().addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    String tuKhoa = pnlSo.getTxtTimKiem().getText().trim();
                    YearMonth ym = pnlSo.getThangHienTai();

                    if (tuKhoa.isEmpty()) {
                        loadDuLieuTheoThang(); // Nếu xóa trống, hiện lại toàn bộ giao dịch của tháng
                    } else {
                        // Gọi hàm tìm kiếm từ DAO
                        List<Object[]> listKetQua = dao.timKiemGiaoDich(userId, ym.getMonthValue(), ym.getYear(), tuKhoa);
                        pnlSo.renderDanhSachGiaoDich(listKetQua); // Vẽ lại danh sách đã lọc
                    }
                }
            });
        }
        // =====================================================================
    }

    // --- Hàm xử lý lưu Thu biệt lập ---
    private void handleLuuThu() {
        try {
            double soTien = Double.parseDouble(pnlThem.getTxtSoTienThu().getText().trim());
            String ghiChu = pnlThem.getTxtGhiChuThu().getText().trim();
            String danhMuc = pnlThem.getCbDanhMucThu().getSelectedItem().toString();

            if (dao.insertGiaoDich(soTien, ghiChu, danhMuc, userId)) {
                JOptionPane.showMessageDialog(pnlThem, "Thêm khoản thu thành công!");
                thuBangThem();
                loadDuLieuTheoThang();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlThem, "Số tiền nhập vào không hợp lệ!");
        }
        pnlThem.getTxtSoTienThu().setText("");
        pnlThem.getTxtGhiChuThu().setText("");
    }

    // --- Hàm xử lý lưu Chi biệt lập ---
    private void handleLuuChi() {
        try {
            double soTien = Double.parseDouble(pnlThem.getTxtSoTienChi().getText().trim());
            String ghiChu = pnlThem.getTxtGhiChuChi().getText().trim();
            String danhMuc = pnlThem.getCbDanhMucChi().getSelectedItem().toString();

            if (dao.insertGiaoDich(soTien, ghiChu, danhMuc, userId)) {
                JOptionPane.showMessageDialog(pnlThem, "Thêm khoản chi thành công!");
                thuBangThem();
                loadDuLieuTheoThang();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlThem, "Số tiền nhập vào không hợp lệ!");
        }
        pnlThem.getTxtSoTienThu().setText("");
        pnlThem.getTxtGhiChuThu().setText("");
    }

    private void thuBangThem() {
        // Tìm cửa sổ chính MainFrame
        java.awt.Window window = SwingUtilities.getWindowAncestor(pnlThem);

        if (window instanceof MainFrame) {
            MainFrame mainFrame = (MainFrame) window;

            // Gọi lệnh ép ẩn trực tiếp từ MainFrame ra
            mainFrame.closeBottomSheet();
        }

        // Cập nhật lại số liệu tiền nong trên sổ giao dịch ngay lập tức
        loadDuLieuTheoThang();
    }

    public void loadDuLieuTheoThang() {
        YearMonth ym = pnlSo.getThangHienTai();
        List<Object[]> listGD = dao.getGiaoDichTheoThang(userId, ym.getMonthValue(), ym.getYear());

        double tongThu = 0, tongChi = 0;
        for (Object[] row : listGD) {
            double soTien = (Double) row[1];
            String tenDM = row[3].toString();
            if (tenDM.contains("Lương") || tenDM.contains("Thu nhập") || tenDM.contains("Được tặng")) {
                tongThu += soTien;
            } else {
                tongChi += soTien;
            }
        }

        pnlSo.getLblTongThu().setText(String.format("%,.0f đ", tongThu));
        pnlSo.getLblTongChi().setText(String.format("%,.0f đ", tongChi));
        pnlSo.getLblSoDu().setText(String.format("%,.0f đ", tongThu - tongChi));
        pnlSo.renderDanhSachGiaoDich(listGD);
    }
}
