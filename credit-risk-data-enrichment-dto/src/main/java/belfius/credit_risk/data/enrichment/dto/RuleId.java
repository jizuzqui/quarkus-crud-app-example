package belfius.credit_risk.data.enrichment.dto;

public class RuleId implements java.io.Serializable {

	private long id;
	private int version;

	public RuleId() {
	}

	public RuleId(long id, int version) {
		this.id = id;
		this.version = version;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RuleId))
			return false;
		RuleId castOther = (RuleId) other;

		return (this.getId() == castOther.getId()) && (this.getVersion() == castOther.getVersion());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getId();
		result = 37 * result + this.getVersion();
		return result;
	}

}
