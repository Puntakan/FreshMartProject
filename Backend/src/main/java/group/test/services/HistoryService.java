package group.test.services;

import group.test.dtos.HistoryDto;
import group.test.entities.History;
import group.test.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    public List<History> getAllPlayers() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return historyRepository.findAll(sort);
    }

    public History findById(Integer id) {
        return historyRepository.findById(id).orElse(null);
    }


    public History createHistory(History history) {
        return historyRepository.save(history);
    }
    public void delete(Integer id) {
        historyRepository.deleteById(id);
    }

    public History updateHistory(Integer id, History updatedHistory) {
        Optional<History> existingHistory = historyRepository.findById(id);

        if (existingHistory.isPresent()) {
            History history = existingHistory.get();
            history.setNumList(updatedHistory.getNumList());
            history.setDateTime(updatedHistory.getDateTime());
            history.setCustomerType(updatedHistory.getCustomerType());
            history.setDiscount(updatedHistory.getDiscount());
            history.setTotal(updatedHistory.getTotal());

            return historyRepository.save(history);
        } else {
            return null;
        }
    }

}