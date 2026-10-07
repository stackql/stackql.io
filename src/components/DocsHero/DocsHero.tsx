import React from "react";
import styles from "./DocsHero.module.css";
import ThemedImage from "@theme/ThemedImage";

type Props = {
  lightSrc: string;
  darkSrc: string;
  alt?: string;
  title: React.ReactNode;   // HEADLINE
  byline: React.ReactNode;  // BYLINE
};

export default function DocsHero({ lightSrc, darkSrc, alt = "", title, byline }: Props) {
  return (
    <section className={styles.wrap} aria-label="StackQL hero">
      <div className={styles.logo}>
        <ThemedImage
          alt={alt}
          sources={{ light: lightSrc, dark: darkSrc }}
        />
      </div>

      <div>
        {/* the page's one H1: docs/index.md sets hide_title, so nothing else renders one */}
        <h1 className={styles.headline}>{title}</h1>
        <br/>
        <div className={styles.byline}>{byline}</div>
      </div>
    </section>
  );
}
