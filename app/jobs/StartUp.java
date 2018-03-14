package jobs;

import org.hibernate.Session;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;


@OnApplicationStart
public class StartUp extends Job {


	@Override
	public void doJob() throws Exception {
		// 启动时的系统操作
		super.doJob();
	}
	
	


	public static void update() {
		Session session = (Session) JPA.em().getDelegate();
		if (!session.getTransaction().isActive()) {
			session.getTransaction().begin();
		}
		//session.createSQLQuery("update WeBook set isVertical = 1 where isVertical is null ").executeUpdate();
		session.getTransaction().commit();

	}


}
