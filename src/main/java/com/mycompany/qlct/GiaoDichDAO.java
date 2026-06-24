package com.mycompany.qlct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GiaoDichDAO {

    // Hàm lấy danh sách giao dịch đổ lên màn hình Sổ Giao Dịch
    public List<Object[]> getGiaoDichTheoThang(String userId, int thang, int nam) {
        List<Object[]> danhSach = new ArrayList<>();

        // Khớp chuẩn 100% tên cột: d.tenDanhMuc, g.ThoiGianGD, d.msdm
        String sql = "SELECT g.MaGD, g.SoTien, g.GhiChu, d.tenDanhMuc, g.ThoiGianGD "
                + "FROM GiaoDich g JOIN DanhMuc d ON g.MaDM = d.msdm "
                + "WHERE g.MaND = ? AND MONTH(g.ThoiGianGD) = ? AND YEAR(g.ThoiGianGD) = ? "
                + "ORDER BY g.ThoiGianGD DESC";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setInt(2, thang);
            ps.setInt(3, nam);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[5];
                    row[0] = rs.getString("MaGD");
                    row[1] = rs.getDouble("SoTien");
                    row[2] = rs.getString("GhiChu");
                    row[3] = rs.getString("tenDanhMuc"); 
                    row[4] = rs.getTimestamp("ThoiGianGD"); 
                    danhSach.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // Hàm thêm mới giao dịch vào CSDL
    public boolean insertGiaoDich(double soTien, String ghiChu, String tenDM, String userId) {
        String sql = "INSERT INTO GiaoDich (MaGD, SoTien, GhiChu, MaDM, MaND, ThoiGianGD) "
                + "VALUES (?, ?, ?, (SELECT msdm FROM DanhMuc WHERE tenDanhMuc = ?), ?, GETDATE())";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String maGD = "GD" + (System.currentTimeMillis() % 100000);
            
            ps.setString(1, maGD);
            ps.setDouble(2, soTien);
            ps.setString(3, ghiChu);
            ps.setString(4, tenDM);
            ps.setString(5, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm cập nhật giao dịch
    public boolean updateGiaoDich(String maGD, double soTien, String tenDM) {
        String sql = "UPDATE GiaoDich SET SoTien = ?, MaDM = (SELECT msdm FROM DanhMuc WHERE tenDanhMuc = ?) WHERE MaGD = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, soTien);
            ps.setString(2, tenDM);
            ps.setString(3, maGD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm xóa giao dịch
    public boolean deleteGiaoDich(String maGD) {
        String sql = "DELETE FROM GiaoDich WHERE MaGD = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maGD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm lấy toàn bộ tên danh mục để đổ vào ComboBox chọn
    public List<String> getDanhSachTenDanhMuc() {
        List<String> danhSach = new ArrayList<>();
        // Sửa dứt điểm TenDM thành tenDanhMuc tại đây
        String sql = "SELECT tenDanhMuc FROM DanhMuc";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                danhSach.add(rs.getString("tenDanhMuc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // Hàm tìm kiếm giao dịch theo từ khóa
    public List<Object[]> timKiemGiaoDich(String userId, int thang, int nam, String tuKhoa) {
        List<Object[]> danhSach = new ArrayList<>();
        String sql = "SELECT g.MaGD, g.SoTien, g.GhiChu, d.tenDanhMuc, g.ThoiGianGD "
                + "FROM GiaoDich g JOIN DanhMuc d ON g.MaDM = d.msdm "
                + "WHERE g.MaND = ? AND MONTH(g.ThoiGianGD) = ? AND YEAR(g.ThoiGianGD) = ? "
                + "AND (g.GhiChu LIKE ? OR d.tenDanhMuc LIKE ?) "
                + "ORDER BY g.ThoiGianGD DESC";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            ps.setString(4, "%" + tuKhoa + "%");
            ps.setString(5, "%" + tuKhoa + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[5];
                    row[0] = rs.getString("MaGD");
                    row[1] = rs.getDouble("SoTien");
                    row[2] = rs.getString("GhiChu");
                    row[3] = rs.getString("tenDanhMuc");
                    row[4] = rs.getTimestamp("ThoiGianGD");
                    danhSach.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }
}