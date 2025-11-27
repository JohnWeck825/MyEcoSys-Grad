package com.example.myecosysgrad.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.Id;

import lombok.*;

/**
 * BaseEntity — lớp cha dùng chung cho tất cả Entity trong dự án. Giúp tránh lặp lại các hàm
 * equals(), hashCode() và đảm bảo hoạt động đúng cả khi Hibernate sử dụng cơ chế proxy (đặc biệt
 * khi FetchType.LAZY).
 */
@Getter
@Setter
@MappedSuperclass // 👈 đánh dấu đây là lớp cha cho các Entity kế thừa
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    // ================================================================
    // 1️⃣ Hàm equals() — so sánh 2 entity dựa trên id, có xử lý proxy
    // ================================================================
    @Override
    public final boolean equals(Object o) {
        // Nếu cùng tham chiếu (cùng địa chỉ vùng nhớ) => true
        if (this == o) return true;

        // Nếu o == null => false
        if (o == null) return false;

        // Nếu 1 trong 2 là HibernateProxy, ta phải "unwrap" (bóc lớp proxy ra)
        // Hibernate có thể tạo lớp con giả như Role$HibernateProxy$123
        // mà không equals được với Role thật
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();

        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();

        // Nếu 2 class thực sự khác nhau (Role vs Permission chẳng hạn) => false
        if (thisEffectiveClass != oEffectiveClass) return false;

        // Ép kiểu và so sánh theo id (id != null để tránh so sánh entity chưa persist)
        BaseEntity that = (BaseEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    // ================================================================
    // 2️⃣ Hàm hashCode() — sinh mã hash ổn định kể cả khi entity bị proxy
    // ================================================================
    @Override
    public final int hashCode() {
        // Nếu entity đang ở dạng proxy, lấy class gốc để sinh hash
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this)
                        .getHibernateLazyInitializer()
                        .getPersistentClass()
                        .hashCode()
                : getClass().hashCode();
    }

    // ================================================================
    // 3️⃣ Hàm toString() — tùy chọn (giúp debug dễ hơn, tránh vòng lặp vô hạn)
    // ================================================================
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(id=" + id + ")";
    }
}
