package belfius.credit_risk.data.enrichment.dto;

public class Rule implements java.io.Serializable {

	private int id;
	private int version;
	private int statusId;
	private int familyId;
	private int scopeId;
	private int priority;
	private String sentence;
	private String description;

	public Rule() {
	}

	public Rule(int id, int version, int statusId, int familyId, int scopeId, int priority, String sentence) {
		this.id = id;
		this.version = version;
		this.statusId = statusId;
		this.familyId = familyId;
		this.scopeId = scopeId;
		this.priority = priority;
		this.sentence = sentence;
	}

	public Rule(int id, int version, int statusId, int familyId, int scopeId, int priority, String sentence, String description) {
		this.id = id;
		this.version = version;
		this.statusId = statusId;
		this.familyId = familyId;
		this.scopeId = scopeId;
		this.priority = priority;
		this.sentence = sentence;
		this.description = description;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getStatusId() {
		return this.statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getFamilyId() {
		return this.familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	public int getScopeId() {
		return this.scopeId;
	}

	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getSentence() {
		return this.sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "{\"id\": " + id + ", \"version\": " + version + ", \"statusId\": " + statusId + ", \"scopeId\": " + scopeId + ", \"sentence\": " + sentence 
			+ ", \"priority\": " + priority + ", \"familyId\": " + familyId + "}";
	}
}
