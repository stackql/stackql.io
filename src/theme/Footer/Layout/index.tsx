/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import React from 'react';
import clsx from 'clsx';
import type {Props} from '@theme/Footer/Layout';
import type { JSX } from 'react';

export default function FooterLayout({
  style,
  links,
  logo,
  copyright,
}: Props): JSX.Element {
  return (
    <footer
      className={clsx('footer', {
        'footer--dark': style === 'dark',
      })}>
      <div className="container container-fluid">
        {links}
        {(logo || copyright) && (
          <div className="footer__bottom text--center">
            {logo && (
              <div className="margin-bottom--sm">
                {logo}
                <div style={{ marginTop: '1rem' }}>
                  <p style={{ fontSize: '0.9rem', color: 'var(--ifm-footer-color)', marginBottom: '0.75rem' }}>
                    A new approach to querying and provisioning cloud services.
                  </p>
                  <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', alignItems: 'center' }}>
                    <img src="/img/aaif_memberbadge_silver.svg" alt="AAIF Member" style={{ height: '50px' }} />
                    <img src="/img/LF_MemberLevel_silver.svg" alt="Linux Foundation Member" style={{ height: '50px' }} />
                  </div>
                </div>
              </div>
            )}
            {copyright}
          </div>
        )}
      </div>
    </footer>
  );
}
