package SafetyNet.alerts.repositorys;

import SafetyNet.alerts.models.Data;
import org.springframework.stereotype.Repository;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {

    private final Data data;

    public MedicalRecordRepositoryImpl(Data data) {
        this.data = data;
    }
}
