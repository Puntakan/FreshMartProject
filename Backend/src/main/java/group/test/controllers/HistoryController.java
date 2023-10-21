package group.test.controllers;


import group.test.dtos.HistoryDto;
import group.test.entities.History;
import group.test.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins= {"http://127.0.0.1:5173"})
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("")
    public List<History> getAllHistory() {
        return historyService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<History> getHistoryById(@PathVariable Integer id) {
        History history = historyService.findById(id);
        if (history != null) {
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<History> createHistory(@RequestBody HistoryDto historyDto) {
        // ตรวจสอบข้อมูลที่ต้องการแนบ
        if (historyDto.getNumList() != null) {
            try {
                // แปลง HistoryDto เป็น History entity
                History history = new History();
                history.setNumList(historyDto.getNumList());
                history.setDateTime(ZonedDateTime.now()); // กำหนดเวลาใหม่
                history.setCustomerType(historyDto.getCustomerType());
                history.setDiscount(historyDto.getDiscount());
                history.setTotal(historyDto.getTotal());

                // บันทึกลงในฐานข้อมูล
                History createdHistory = historyService.createHistory(history);
                return new ResponseEntity<>(createdHistory, HttpStatus.CREATED);
            } catch (Exception e) {
                // จัดการข้อผิดพลาดในการบันทึก
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<History> updateHistory(@PathVariable Integer id, @RequestBody History updatedHistory) {
        History updated = historyService.updateHistory(id, updatedHistory);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Integer id) {
        historyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
