package com.pagamento.common.health;



import java.util.Iterator;

import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.NamedContributor;

public class AutoConfiguredHealthContributorRegistry implements HealthContributorRegistry {

	@Override
	public void registerContributor(String name, HealthContributor contributor) {
		// TODO Auto-generated method stub

	}

	@Override
	public HealthContributor unregisterContributor(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HealthContributor getContributor(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<NamedContributor<HealthContributor>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
