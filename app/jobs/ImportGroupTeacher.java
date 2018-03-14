package jobs;

import models.WePerson;
import models.group.GroupMember;
import models.group.OrganizeGroup;
import models.member.Teacher;
import org.apache.commons.codec.digest.DigestUtils;
import play.jobs.Job;
import utils.ExcelUtil;

import java.io.File;

public class ImportGroupTeacher extends Job {
	public File file;
	public OrganizeGroup group;
	public StringBuffer repeatCellPhone;
	public StringBuffer repeatEamil;

	public ImportGroupTeacher(File file, OrganizeGroup group) {
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
			Teacher member = new Teacher();
			//member.number = data[i][0];
			member.name = data[i][1];
			member.password = DigestUtils.md5Hex("111111");
			member.job = data[i][2];
			member.cellPhone = data[i][4];
			if (!WePerson.isPhoneAvailable(member.cellPhone)) {
				repeatCellPhone.append(member.cellPhone).append(",");
				continue;
			}
			/*
			 * if (StringUtils.isBlank(member.email)) { member.email =
			 * member.cellPhone + "@163.com"; } if
			 * (!WePerson.isEmailAvailable(member.email)) {
			 * repeatEamil.append(member.email).append(","); continue; }
			 */
			Teacher teacher = Teacher.regist(member);
			GroupMember.createAssociation(group, teacher);
		}

	}
}
