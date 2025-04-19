package ru.hackathon.finmonitor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hackathon.finmonitor.exception.FinmonitorErrorType;
import ru.hackathon.finmonitor.exception.FinmonitorException;
import ru.hackathon.finmonitor.model.Transaction;
import ru.hackathon.finmonitor.model.TransactionStatus;
import ru.hackathon.finmonitor.repository.TransactionRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    @Test
    void delete_NewTransaction_ChangesStatusToDeleted() {
        // Given.
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.NEW);
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));
        when(repository.save(any())).thenReturn(transaction);

        // When.
        service.delete(1L);

        // Then.
        verify(repository).save(transaction);
        assertEquals(TransactionStatus.DELETED, transaction.getStatus());
    }

    @Test
    void delete_ConfirmedTransaction_ThrowsException() {
        // Given.
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.CONFIRMED);
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        // When & then.
        FinmonitorException exception = assertThrows(FinmonitorException.class, () -> service.delete(1L));
        assertEquals(FinmonitorErrorType.TRANSACTION_DELETION_FORBIDDEN, exception.getErrorType());
        verify(repository, never()).save(any());
    }

    @Test
    void delete_InProcessTransaction_ThrowsException() {
        // Given.
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.IN_PROCESS);
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        // When & then.
        FinmonitorException exception = assertThrows(FinmonitorException.class, () -> service.delete(1L));
        assertEquals(FinmonitorErrorType.TRANSACTION_DELETION_FORBIDDEN, exception.getErrorType());
        verify(repository, never()).save(any());
    }

    @Test
    void delete_CancelledTransaction_ThrowsException() {
        // Given.
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.CANCELED);
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        // When & then.
        FinmonitorException exception = assertThrows(FinmonitorException.class, () -> service.delete(1L));
        assertEquals(FinmonitorErrorType.TRANSACTION_DELETION_FORBIDDEN, exception.getErrorType());
        verify(repository, never()).save(any());
    }

    @Test
    void delete_CompletedTransaction_ThrowsException() {
        // Given.
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.COMPLETED);
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        // When & then.
        FinmonitorException exception = assertThrows(FinmonitorException.class, () -> service.delete(1L));
        assertEquals(FinmonitorErrorType.TRANSACTION_DELETION_FORBIDDEN, exception.getErrorType());
        verify(repository, never()).save(any());
    }

    @Test
    void delete_RefundTransaction_ThrowsException() {
        // Given.
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.RETURNED);
        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        // When & then.
        FinmonitorException exception = assertThrows(FinmonitorException.class, () -> service.delete(1L));
        assertEquals(FinmonitorErrorType.TRANSACTION_DELETION_FORBIDDEN, exception.getErrorType());
        verify(repository, never()).save(any());
    }

    @Test
    void delete_NonExistentTransaction_ThrowsException() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // When & then.
        FinmonitorException exception = assertThrows(FinmonitorException.class, () -> service.delete(1L));
        assertEquals(FinmonitorErrorType.NOT_FOUND, exception.getErrorType());
        verify(repository, never()).save(any());
    }
} 