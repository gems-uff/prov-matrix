package convertion;

import java.util.List;
import java.util.Map;

import model.ProvMatrix;

public interface ProvMatrixFactory {
	
	public List<ProvMatrix> buildMatrices(boolean deriveInfluence);

	public Map<String, String> getLabels();

}
