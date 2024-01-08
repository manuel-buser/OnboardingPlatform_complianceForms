package OnboardingPlatform.complianceForms.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LogoReader {
    public BufferedImage readLogo() throws IOException {
        BufferedImage companyLogo = null;
        try {
            // Load the image file
            File logoFile = new File(getClass().getResource("/static/CompanyLogo.jpg").getFile());
            companyLogo = ImageIO.read(logoFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return companyLogo;
    }
}
