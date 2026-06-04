import React, { useEffect, useRef } from 'react';

export default function Gist({ id, file }) {
  const iframeRef = useRef(null);
  const elementId = file ? `gist-${id}-${file}` : `gist-${id}`;
  const src = `https://gist.github.com/${id}.js${file ? `?file=${file}` : ''}`;

  useEffect(() => {
    const iframe = iframeRef.current;
    if (!iframe) return;
    const doc = iframe.contentDocument || iframe.contentWindow?.document;
    if (!doc) return;
    const resize = `onload="parent.document.getElementById('${elementId}').style.height=document.body.scrollHeight + 'px'"`;
    doc.open();
    doc.writeln(
      `<html><head><base target="_parent"><style>*{font-size:12px;}</style></head><body ${resize}><script type="text/javascript" src="${src}"></script></body></html>`
    );
    doc.close();
  }, [id, file, elementId, src]);

  return (
    <iframe
      ref={iframeRef}
      width="100%"
      frameBorder={0}
      id={elementId}
      title={elementId}
    />
  );
}
