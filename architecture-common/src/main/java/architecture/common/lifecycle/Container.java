package architecture.common.lifecycle;

import architecture.common.exception.ComponentNotFoundException;

/**
 * 
 * @author andang son
 *
 */
public interface Container {

	/**
	 * Injection 방식으로 인자로 넘겨진 객체의 프로퍼티를 세닝하여 리턴한다.
	 * 이 동작은 함수 이름이 보여주듯이 자동으로 처리된다.
	 * 이를 위하여 클래스는 자바빈즈 스타일을 준수하여야 한다.
	 * 
	 * @param obj
	 */
	public void autowireComponent(Object obj);
	
	/**
	 * 인자로 넘겨진 클래스를 생성하고 자동으로 클래스의 프로퍼티를 Injection 방식으로 세팅한다.
	 * 이를 위하여 클래스는 자바빈즈 스타일을 준수하여야 한다.
	 * @param requiredType
	 * @return
	 */
	public <T> T createComponent(Class<T> requiredType);
	
	/**
	 * 인자에 해당하는 객체를 검색하여 리턴한다. 인자는 클래스 또는 객체를 구분가능하게 하는 이름이 될 수 있다.
	 * @param obj
	 * @return
	 * @throws ComponentNotFoundException
	 */
	public Object getComponent(Object obj) throws ComponentNotFoundException;
	
	
	
	
	
	/**
	 * 인자에 해당하는 타입의 객체를 리턴한다. 
	 * @param <T>
	 * @param requiredType 서비스 클래스
	 * @return
	 * @throws ComponentNotFoundException
	 */
	public <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException;
	
	public <T> T getComponent(String requiredName, Class<T> requiredType)throws ComponentNotFoundException;
	
	public Object getInstance(Object obj);
	
	/**
	 * 
	 */
	public void refresh();
	
}
