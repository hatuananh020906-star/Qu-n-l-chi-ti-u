/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.qlct;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
/**
 *
 * @author lozim
 */
public class RoundedPanel extends JPanel {
    
    // Bạn có thể đổi số 30 này để góc cong nhiều hay ít tùy ý
    private int cornerRadius = 30; 

    public RoundedPanel() {
        setOpaque(false); // Bắt buộc phải có để viền ngoài bị cắt gọt trong suốt
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Bật tính năng khử răng cưa cho viền cực kỳ mịn màng
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Lấy màu nền bạn đã cài đặt để tô màu
        g2.setColor(getBackground());
        
        // Vẽ hình chữ nhật bo góc
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}
