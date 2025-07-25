package com.unitech.transfer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fromIban;
    private String toIban;
    private BigDecimal amount;
    private String status; //  "COMPLETED", "FAILED"
    private String failureReason;

    @CreationTimestamp
    private LocalDateTime createdAt;
}