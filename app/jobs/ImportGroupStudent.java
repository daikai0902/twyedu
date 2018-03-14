package jobs;


import models.group.Clazz;
import models.group.GroupMember;
import models.member.Student;
import play.jobs.Job;
import utils.ExcelUtil;

import java.io.File;

public class ImportGroupStudent extends Job {
	public File file;
	public Clazz group;
	public StringBuffer repeatCellPhone;
	public StringBuffer repeatEamil;

	public ImportGroupStudent(File file, Clazz group) {
		super();
		this.file = file;
		this.group = group;
		this.repeatCellPhone = new StringBuffer();
		this.repeatEamil = new StringBuffer();
	}

	@Override
	public void doJob() throws Exception {
		String[][] data = ExcelUtil.getData(file, 1).get(0);
		for (int i = 0; i < data.length; i++) {
			Student member = new Student();
			Student student = Student.registZhuji(member);
			GroupMember.createAssociation(group, student);

		}

	}
}
