package com.mobiquityinc.packer;

import com.mobiquityinc.packer.exception.APIException;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

public class PackerTest {

    @Test
    @Ignore
    /*
      Ignored because it depends on work directory path
     */
    public void testIfAllWorks() {
        Assertions
                .assertThat(Packer.pack("src/test/resources/test.data"))
                .isNotNull()
                .isNotBlank()
                .isEqualTo("4\n-\n2,7\n8,9");
    }

    @Test
    public void testIfFileIsBad() {
        Assertions
                .assertThatThrownBy(()-> Packer.pack("invalid/path/to/file"))
                .isExactlyInstanceOf(APIException.class);
    }
}
