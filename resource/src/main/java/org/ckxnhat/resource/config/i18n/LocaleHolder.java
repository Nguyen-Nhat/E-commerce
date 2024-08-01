package org.ckxnhat.resource.config.i18n;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-26 21:26:51.736
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocaleHolder {
    private Locale currentLocale;
}
