package ams.com.eattendance.domain.repository;

import java.io.IOException;

public interface WebServiceRepository {

	Boolean getCompanyByName(String name, String phoneNumber) throws IOException;
}
