package convertion;

import java.util.List;

import model.ProvMatrix;

public interface ProvMatrixFactory {
	
	public List<ProvMatrix> buildMatrices(boolean deriveInfluence);

}
