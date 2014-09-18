package architecture.ee.jdbc.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionHelper extends DefaultTransactionDefinition {

	private static final long serialVersionUID = 1549464827761296884L;
	
	private PlatformTransactionManager transactionManager ;

	public TransactionHelper(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
		
	public TransactionHelper(PlatformTransactionManager transactionManager, TransactionDefinition transactionDefinition) {
		super(transactionDefinition);
		this.transactionManager = transactionManager;
	}
	public TransactionStatus startTransaction(){
		return transactionManager.getTransaction(this);		
	}
	
	public void rollback(TransactionStatus status){
		transactionManager.rollback(status);
	}
	
	public void commit(TransactionStatus status){
		transactionManager.commit(status);		
	}
	
}
