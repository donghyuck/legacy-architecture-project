package architecture.common.scanner;

import java.io.File;
import java.util.EventListener;

public interface DirectoryListener extends EventListener {

	public abstract boolean isFileDeployed(File file);

	public abstract long getDeploymentTime(File file);
	
	public abstract boolean validateFile(File file);

	public abstract String fileCreated(File file);

	public abstract boolean fileDeleted(File file);

	public abstract void fileChanged(File file);

}
