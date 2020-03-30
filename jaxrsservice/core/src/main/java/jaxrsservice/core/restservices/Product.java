package jaxrsservice.core.restservices;

public class Product {

	private String id;
	private String name;
	private String title;
	private String category;
	private String desc;
	private String result;

	public void setId(String id)
	{
		this.id=id;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public void setTitle(String title)
	{
		this.title=title;
	}
	public void setCategory(String category)
	{
		this.category=category;
	}
	public void setDesc(String desc)
	{
		this.desc=desc;
	}
	
	public void setResult(String result)
	{
		this.result=result;
	}

	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public String getTitle()
	{
		return title;
	}
	public String getCategory()
	{
		return category;
	}
	public String getDesc()
	{
		return desc;
	}
	
	public String getResult()
	{
		return result;
	}


}