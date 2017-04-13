package ams.com.eattendance.domain.repository;

import java.io.IOException;

/**
 * Created by WeConnect on 4/12/2017.
 */
public interface WebServiceRepository {

	Boolean getCompanyByName(String name,String phoneNumber) throws IOException;
}
