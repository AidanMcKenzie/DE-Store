public class RetailBranch 
{
	// Attributes of Student object
	private String matric;
	private String name;
	private String programme;
	
	/**
	 * Default constructor
	 * 
	 * @param matric The Student's matric number
	 * @param name The Student's name
	 * @param programme The Student's programme
	 */
	public RetailBranch(String matric, String name, String programme)
	{
		this.matric = matric;
		this.name = name;
		this.programme = programme;
	}
	
	/**
	 * Gets the Student's matric number
	 * 
	 * @return The matric number of the student
	 */
	public String getMatric()
	{
		return matric;
	}
	
	/**
	 * Changes the Student's matric number
	 * 
	 * @param matric The new matric number
	 */
	public void setMatric(String matric)
	{
		this.matric = matric;
	}
	
	/**
	 * Gets the Student's name
	 * 
	 * @return The name of the Student
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Changes the Student's name
	 * 
	 * @param name The new name of the Student
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Gets the Student's programme
	 * 
	 * @return The programme of the Student
	 */
	public String getProgramme()
	{
		return programme;
	}
	
	/**
	 * changes the Student's programme
	 * 
	 * @param programme The new programme of the Student
	 */
	public void setProgramme(String programme)
	{
		this.programme = programme;
	}
}
