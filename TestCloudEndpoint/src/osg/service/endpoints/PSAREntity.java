package osg.service.endpoints;

public class PSAREntity {

	private String id;
	private String name;

	private long numCommToFanPost;
	private long numCommToOwnPost;
	private long numLikeToOwnPost;
	private long numOtherPost;
	private long numOwnPost;
	private long numShareToOwnPost;
	private long numOwnComm;
	
	
	
	
	public PSAREntity(String id, String name, long numCommToFanPost,
			long numCommToOwnPost, long numLikeToOwnPost, long numOtherPost,
			long numOwnPost, long numShareToOwnPost, long numOwnComm) {
		super();
		this.id = id;
		this.name = name;
		this.numCommToFanPost = numCommToFanPost;
		this.numCommToOwnPost = numCommToOwnPost;
		this.numLikeToOwnPost = numLikeToOwnPost;
		this.numOtherPost = numOtherPost;
		this.numOwnPost = numOwnPost;
		this.numShareToOwnPost = numShareToOwnPost;
		this.numOwnComm = numOwnComm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getNumCommToFanPost() {
		return numCommToFanPost;
	}
	public void setNumCommToFanPost(long numCommToFanPost) {
		this.numCommToFanPost = numCommToFanPost;
	}
	public long getNumCommToOwnPost() {
		return numCommToOwnPost;
	}
	public void setNumCommToOwnPost(long numCommToOwnPost) {
		this.numCommToOwnPost = numCommToOwnPost;
	}
	public long getNumLikeToOwnPost() {
		return numLikeToOwnPost;
	}
	public void setNumLikeToOwnPost(long numLikeToOwnPost) {
		this.numLikeToOwnPost = numLikeToOwnPost;
	}
	public long getNumOtherPost() {
		return numOtherPost;
	}
	public void setNumOtherPost(long numOtherPost) {
		this.numOtherPost = numOtherPost;
	}
	public long getNumOwnPost() {
		return numOwnPost;
	}
	public void setNumOwnPost(long numOwnPost) {
		this.numOwnPost = numOwnPost;
	}
	public long getNumShareToOwnPost() {
		return numShareToOwnPost;
	}
	public void setNumShareToOwnPost(long numShareToOwnPost) {
		this.numShareToOwnPost = numShareToOwnPost;
	}
	public long getNumOwnComm() {
		return numOwnComm;
	}
	public void setNumOwnComm(long numOwnComm) {
		this.numOwnComm = numOwnComm;
	}
	
	

	



}