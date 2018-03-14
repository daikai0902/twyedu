package models.group;

import models.BaseModel;
import models.WePerson;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.*;

/**
 * 机构成员
 * 
 * @author daikai
 *
 */
@Entity
public class GroupMember extends BaseModel {
	@ManyToOne
	public WePerson person;
	@ManyToOne
	public AbstractGroup abstractGroup;


	public static GroupMember createAssociation(AbstractGroup group, WePerson person) {
		GroupMember gm = findByGroupAndPerson(group, person);
		if (gm == null) {
			gm = new GroupMember();
			gm.abstractGroup = group;
			gm.person = person;
			gm.save();
		}
		return gm;
	}

	public static void createAssociations(AbstractGroup group, List<WePerson> persons) {
		for (WePerson person : persons) {
			createAssociation(group, person);
		}
	}

	public static GroupMember findByGroupAndPerson(AbstractGroup group, WePerson person) {
		String hql = getDefaultContitionSql("select gm from GroupMember gm where gm.abstractGroup=? and gm.person = ?");
		return GroupMember.find(hql, group, person).first();
	}

	// 精确到机构
	public static GroupMember findByOrgAndPerson(OrganizeGroup group, WePerson person) {
		String hql = getDefaultContitionSql(
				"select gm from GroupMember gm where gm.abstractGroup.rootGroup=? and gm.person = ?");
		return GroupMember.find(hql, group, person).first();
	}

	public static List<WePerson> getAllMembersByGroup(AbstractGroup group) {
		String hql = getDefaultContitionSql("select gm.person from GroupMember gm where gm.abstractGroup=? ");
		return GroupMember.find(hql, group).fetch();
	}

	public static List<GroupMember> fetchByGroup(AbstractGroup group) {
		String hql = getDefaultContitionSql("select gm from GroupMember gm where gm.abstractGroup=? ");
		return GroupMember.find(hql, group).fetch();
	}

	public static List<WePerson> fetchStudentsByGroup(AbstractGroup group) {
		String hql = getDefaultContitionSql(
				"select gm.person from GroupMember gm where gm.abstractGroup=? and gm.person.class='Student'");
		return GroupMember.find(hql, group).fetch();
	}

	public static List<WePerson> fetchStudentsByGroups(List<Clazz> groups) {
		String hql = getDefaultContitionSql(
				"select gm.person from GroupMember gm where gm.abstractGroup in(:groups) and gm.person.class='Student' order by gm.person.lastModifyTime");
		return GroupMember.find(hql).bind("groups", groups.toArray()).fetch();
	}

	public static List<WePerson> fetchStudentsByClazz(Clazz clazz) {
		String hql = getDefaultContitionSql(
				"select gm.person from GroupMember gm where gm.abstractGroup = ? and gm.person.class='Student' order by gm.person.lastModifyTime");
		return GroupMember.find(hql,clazz).fetch();
	}

	public static Long countStudentsByClazz(Clazz clazz) {
		String hql = getDefaultContitionSql(
				"select count(gm.person) from GroupMember gm where gm.abstractGroup = ? and gm.person.class='Student' ");
		return GroupMember.find(hql,clazz).first();
	}

	public static List<Long> fetchStudentsId(Long groupId) {
		String hql = getDefaultContitionSql(
				"select distinct gm.person.id from GroupMember gm where gm.abstractGroup.id=? and gm.person.class='Student' order by gm.person.lastModifyTime");
		return GroupMember.find(hql, groupId).fetch();
	}

	// 获取一个机构下的所有学生ID
	public static List<Long> fetchOneGroupStudentsId(List<Long> childIds) {
		String hql = getDefaultContitionSql(
				"select distinct gm.person.id from GroupMember gm where gm.abstractGroup.isDeleted=false"
						+ " and gm.abstractGroup.id in(:childIds) and gm.person.class='Student' ");
		return GroupMember.find(hql).bind("childIds", childIds.toArray()).fetch();
	}

	// 获取一个机构下的所有学生
	public static List<WePerson> fetchOneGroupStudents(List<Long> childIds) {
		String hql = getDefaultContitionSql(
				"select distinct gm.person from GroupMember gm where gm.abstractGroup.isDeleted=false"
						+ " and gm.abstractGroup.id in(:childIds) and gm.person.class='Student' ");
		return GroupMember.find(hql).bind("childIds", childIds.toArray()).fetch();
	}

	public static List<WePerson> fetchTeachersByGroup(AbstractGroup group) {
		String hql = getDefaultContitionSql(
				"select gm.person from GroupMember gm where gm.abstractGroup=? and gm.person.class='Teacher' ");
		return GroupMember.find(hql, group).fetch();
	}

	//海曙smooc 机构首页的老师展示
	public static List<WePerson> fetchActiveTeachersByGroup(AbstractGroup group) {
		String hql = getDefaultContitionSql(
				"select gm.person from GroupMember gm where gm.abstractGroup=? and gm.person.class='Teacher' order by (gm.person.courseNum+gm.person.interestGroupNum) desc");
		return GroupMember.find(hql, group).fetch();
	}

	public static List<Long> fetchTeacherIdsByGroup(AbstractGroup group) {
		String hql = getDefaultContitionSql(
				"select gm.person.id from GroupMember gm where gm.abstractGroup=? and gm.person.class='Teacher' ");
		return GroupMember.find(hql, group).fetch();
	}

	public static long count(long groupId) {
		String hql = "select count(gm.id) from GroupMember gm where gm.abstractGroup.id=? and gm.isDeleted=false and gm.abstractGroup.isDeleted=false";
		return GroupMember.count(hql, groupId);
	}

	public static List<GroupMember> fetchByGroupAndClass(AbstractGroup group, String personClass) {
		String hql = getDefaultContitionSql(
				"select gm from GroupMember gm where gm.abstractGroup=? and gm.person.class=? ");
		return GroupMember.find(hql, group, personClass).fetch();
	}

	public static AbstractGroup fetchOrganizeByMember(WePerson person) {
		return GroupMember
				.find(" select gm.abstractGroup from GroupMember gm where  gm.abstractGroup.class='OrganizeGroup' and gm.person=?",
						person)
				.first();
	}

	// 教师可能加入机构的同时加入教研组
	public static List<AbstractGroup> fetchOrganizesByMember(WePerson person) {
		return GroupMember
				.find(" select gm.abstractGroup from GroupMember gm where  gm.abstractGroup.class='OrganizeGroup' and gm.person=? ",
						person)
				.fetch();

	}

	public static AbstractGroup findByStudent(WePerson person) {
		return GroupMember
				.find(" select gm.abstractGroup from GroupMember gm where  gm.abstractGroup.class='Clazz' and gm.person=? and gm.isDeleted=false and gm.abstractGroup.isFile = 0  ",
						person)
				.first();
	}

	public static List<Long> findByPersonId(List<Long> personIds) {
		if (personIds.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		TreeSet<Long> personId = new TreeSet<>(personIds);
		List<Long> groupId = GroupMember
				.find(" select gm.abstractGroup.id from GroupMember gm where gm.person.id in(:personIds) and gm.abstractGroup.class='Clazz' and gm.isDeleted = false")
				.bind("personIds", personId.toArray()).fetch();
		return groupId;
	}

	public static Map<Long, String> findClazzByPersonId(List<Long> personIds) {
		if (personIds.isEmpty()) {
			return Collections.EMPTY_MAP;
		}
		TreeSet<Long> personId = new TreeSet<>(personIds);
		List<Object[]> groupList = GroupMember
				.find(" select gm.abstractGroup.id ,gm.abstractGroup.groupName from GroupMember gm where gm.person.id in(:personIds) and gm.abstractGroup.class='Clazz' and gm.isDeleted = false")
				.bind("personIds", personId.toArray()).fetch();
		Map<Long, String> groupMap = new TreeMap<>();
		for (Object[] obj : groupList) {
			groupMap.put((Long) obj[0], (String) obj[1]);
		}
		return groupMap;
	}

	public static List<Long> findByPersonId(Long personId) {
		List<Long> groupId = GroupMember
				.find(" select gm.abstractGroup.id from GroupMember gm where gm.person.id = ? and gm.abstractGroup.class='Clazz' and gm.isDeleted = false",
						personId)
				.fetch();
		return groupId;
	}

	public static List<Clazz> fetchClazzsByTeacher(WePerson person) {
		String hql = getDefaultContitionSql(
				"select gm.abstractGroup from GroupMember gm where gm.abstractGroup.class='Clazz' and gm.person = ? and gm.abstractGroup.isFile = 0 ");
		return GroupMember.find(hql, person).fetch();
	}

	public static List<Clazz> fetchFileClazzsByTeacher(WePerson person) {
		String hql = getDefaultContitionSql(
				"select gm.abstractGroup from GroupMember gm where gm.abstractGroup.class='Clazz' and gm.person = ? and gm.abstractGroup.isFile = 1 ");
		return GroupMember.find(hql, person).fetch();
	}

	public static AbstractGroup findOrganizeByPerson(WePerson person) {
		if (person.isTeacher()) {
			return fetchOrganizeByMember(person);
		} else if (person.isStudent()) {
			AbstractGroup group = findByStudent(person);
			return group.rootGroup;
		} else {
			return null;
		}
	}

	public static List<Long> getPersonIds(List<Long> groupId) {
		if (groupId.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List<Long> personIds = GroupMember
				.find("select g.person.id from GroupMember g where g.person.class='Student' and  g.abstractGroup.id in(:groupId) and g.isDeleted = false"
						+ " and g.person.isDeleted=false and g.abstractGroup.isDeleted = false")
				.bind("groupId", groupId.toArray()).fetch();
		return personIds;
	}



	/**
	 * 查询多个用户和多个组织的关联
	 *
	 */
	public static List<GroupMember> fetchByGenericGroupsAndMembers(List<AbstractGroup> genericGroupList,
                                                                   List<WePerson> memberList) {
		JPAQuery query = GroupMember.find(
				"select gm from GroupMember gm where gm.isDeleted=false and gm.abstractGroup in(:genericGroups) and gm.person in(:members) ");
		query.bind("genericGroups", genericGroupList.toArray());
		query.bind("members", memberList.toArray());
		return query.fetch();
	}

	/**
	 * 删除组织内所有关联，并未自上而下删除，组织本身会自上而下删除
	 *
	 */
	public static void delByGenericGroup(AbstractGroup abstractGroup) {
		for (GroupMember genericGroupMember : GroupMember.fetchByGroup(abstractGroup)) {
			genericGroupMember.logicDelete();
		}
	}

	/**
	 * 自上而下删除批量用户关联
	 *
	 */
	public static void delUp2DownByGenericGroupAndMembers(AbstractGroup genericGroup, List<WePerson> memberList) {
		List<AbstractGroup> genericGroupList = genericGroup.fetchUpToDown();
		List<GroupMember> genericGroupMemberList = fetchByGenericGroupsAndMembers(genericGroupList, memberList);
		for (GroupMember genericGroupMember : genericGroupMemberList) {
			genericGroupMember.logicDelete();
		}

	}
}
