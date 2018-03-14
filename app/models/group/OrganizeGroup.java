package models.group;


import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.List;

@Entity
public class OrganizeGroup extends AbstractGroup {


	public static OrganizeGroup addGroup(String groupName, String colorLogo, String whiteLogo, String groupIntro, String groupLogo) {
		OrganizeGroup group = new OrganizeGroup();
		group.groupName = groupName;
		group.save();
		group.rootGroup = group;
		OrganizeGroup last = OrganizeGroup.find("isDeleted = 0 and parentGroup = null order by indexOrder desc")
				.first();
		group.indexOrder = last == null ? 0 : last.indexOrder;
		group.indexOrder++;
		return group.save();
	}



	public static List<OrganizeGroup> list(String search) {
		search = search == null ? "" : search;
		return OrganizeGroup.find("isDeleted = 0 and groupName like ? order by indexOrder", "%" + search + "%").fetch();
	}

	public static List<OrganizeGroup> listRoot(String search, int page, int pageSize) {
		search = search == null ? "" : search;
		return OrganizeGroup.find("isDeleted = 0 and parentGroup = null and isPlat = 0 and groupName like ? order by indexOrder",
				"%" + search + "%").fetch(page, pageSize);
	}

	public static Long count(String search) {
		search = search == null ? "" : search;
		return OrganizeGroup.count("isDeleted = 0 and groupName like ? ", "%" + search + "%");
	}

	public static Long countRoot(String search) {
		search = search == null ? "" : search;
		return OrganizeGroup.count("isDeleted = 0 and groupName like ? ", "%" + search + "%");
	}

	public static List<OrganizeGroup> fetchAll() {
		return OrganizeGroup
				.find("select org from OrganizeGroup org where org.isDeleted = false  order by createTime desc ")
				.fetch();
	}
	
	
	public static List<OrganizeGroup> fetchAllJoinOrg() {
		return OrganizeGroup
				.find("select org from OrganizeGroup org where org.isDeleted = false and org.isPlat = false  order by createTime desc ")
				.fetch();
	}


	public static List<OrganizeGroup> listRoot() {
		return OrganizeGroup.find("isDeleted = 0 and parentGroup = null").fetch();
	}


	public static OrganizeGroup fetchPlat(){
		return find(getDefaultContitionSql(" isPlat = true ")).first();
	}
	

	public static OrganizeGroup findByName(String name){
		return OrganizeGroup.find(getDefaultContitionSql(" groupName = ? "),name).first();
	}
}
