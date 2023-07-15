package viamcp.loader;

import com.viaversion.viaversion.api.Via;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

import java.io.File;
import java.util.logging.Logger;

public class MCPRewindLoader implements ViaRewindPlatform
{
    public MCPRewindLoader(final File file)
    {
    }
    
    @Override
    public Logger getLogger()
    {
        return Via.getPlatform().getLogger();
    }
}
