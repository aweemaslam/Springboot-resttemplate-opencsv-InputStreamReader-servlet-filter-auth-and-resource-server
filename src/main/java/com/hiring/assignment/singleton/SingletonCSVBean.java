package com.hiring.assignment.singleton;

import com.hiring.assignment.dto.DtoCSVFormatted;
import com.hiring.assignment.dto.DtoCSVNonFormatted;
import com.hiring.assignment.service.ServiceCSVProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Scope(value = "singleton")
public class SingletonCSVBean {

    private static volatile SingletonCSVBean singletonCSVBean;
    private List<DtoCSVFormatted> dtoCsvFormatted;
    ServiceCSVProcessor servicesvCsvProcessor = new ServiceCSVProcessor();

    private SingletonCSVBean() {
    }

    public static SingletonCSVBean getInstance() {
        if (singletonCSVBean == null) {
            singletonCSVBean = new SingletonCSVBean();
        }
        return singletonCSVBean;
    }

    public List<DtoCSVFormatted> getDtoCSVFormatted() throws IllegalStateException, IOException {
        if (dtoCsvFormatted == null) {
            this.setDtoCSV(servicesvCsvProcessor.downloadCSV());
        }
        return dtoCsvFormatted;
    }

    public void setDtoCSV(List<DtoCSVNonFormatted> dtoCSV) {
        this.dtoCsvFormatted = servicesvCsvProcessor.formatDtoCSV(dtoCSV);
    }

}
