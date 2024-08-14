--liquibase formatted sql
--changeset ckxnhat:init-product-service
CREATE TABLE brand (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    slug VARCHAR(255) NOT NULL,
    image_id VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(255),
    created_on TIMESTAMP(6),
    last_modified_by VARCHAR(255),
    last_modified_on TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    slug VARCHAR(255) NOT NULL,
    image_id VARCHAR(255),
    parent_id BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(255),
    created_on TIMESTAMP(6),
    last_modified_by VARCHAR(255),
    last_modified_on TIMESTAMP(6),
    PRIMARY KEY (id)
);


CREATE TABLE attribute_group (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE attribute_name (
    id BIGINT NOT NULL AUTO_INCREMENT,
    attribute_group_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE spu (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    thumbnail_id VARCHAR(255),
    slug VARCHAR(255) NOT NULL,
    brand_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    sort SMALLINT DEFAULT 0,
    min_price FLOAT DEFAULT 0,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(255),
    created_on TIMESTAMP(6),
    last_modified_by VARCHAR(255),
    last_modified_on TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE sku (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL,
    gtin VARCHAR(255),
    price FLOAT DEFAULT 0,
    is_deleted BOOLEAN default FALSE,
    PRIMARY KEY (id)
);
CREATE TABLE spu_sale_attribute_mapping (
    id BIGINT NOT NULL AUTO_INCREMENT,
    spu_id BIGINT NOT NULL,
    attribute_name_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE spu_sale_attribute_value (
    id BIGINT NOT NULL AUTO_INCREMENT,
    spu_sale_attribute_mapping_id BIGINT NOT NULL,
    value TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE spu_sale_attribute_combination (
    id BIGINT NOT NULL AUTO_INCREMENT,
    spu_id BIGINT NOT NULL,
    sku_id BIGINT DEFAULT NULL,
    attribute_key TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE spu_regular_attribute_value(
    id BIGINT NOT NULL AUTO_INCREMENT,
    spu_id BIGINT NOT NULL,
    attribute_name_id BIGINT NOT NULL,
    attribute_value VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE spu_description (
    id BIGINT NOT NULL AUTO_INCREMENT,
    spu_id BIGINT NOT NULL,
    image_id VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE spu_related (
    id BIGINT NOT NULL AUTO_INCREMENT,
    spu_id BIGINT NOT NULL,
    related_spu_id BIGINT(255),
    PRIMARY KEY (id)
);
CREATE TABLE spu_image (
    id BIGINT NOT NULL AUTO_INCREMENT,
    spu_id BIGINT NOT NULL,
    image_id VARCHAR(255),
    PRIMARY KEY (id)
);

ALTER TABLE category ADD CONSTRAINT FK_CATEGORY_TO_CATEGORY FOREIGN KEY(parent_id) REFERENCES category(id);
ALTER TABLE attribute_group ADD CONSTRAINT FK_ATTRIBUTE_GROUP_TO_CATEGORY FOREIGN KEY(category_id) REFERENCES category(id);
ALTER TABLE attribute_name ADD CONSTRAINT FK_ATTRIBUTE_NAME_TO_GROUP FOREIGN KEY(attribute_group_id) REFERENCES attribute_group(id);
ALTER TABLE spu ADD CONSTRAINT FK_SPU_TO_CATEGORY FOREIGN KEY(category_id) REFERENCES category(id);
ALTER TABLE spu ADD CONSTRAINT FK_SPU_TO_BRAND FOREIGN KEY(brand_id) REFERENCES brand(id);
ALTER TABLE spu_image ADD CONSTRAINT FK_SPU_IMAGE_TO_SPU FOREIGN KEY(spu_id) REFERENCES spu(id);
ALTER TABLE spu_description ADD CONSTRAINT FK_SPU_DES_TO_SPU FOREIGN KEY(spu_id) REFERENCES spu(id);
ALTER TABLE spu_related ADD CONSTRAINT FK_SPU_RELATED_TO_SPU_BY_SPU_ID FOREIGN KEY(spu_id) REFERENCES spu(id);
ALTER TABLE spu_related ADD CONSTRAINT FK_SPU_RELATED_TO_SPU_BY_RELATED_SPU_ID FOREIGN KEY(related_spu_id) REFERENCES spu(id);

ALTER TABLE spu_regular_attribute_value ADD CONSTRAINT FK_SPU_REGULAR_ATTR_VALUE_TO_ATTR_NAME FOREIGN KEY(attribute_name_id) REFERENCES attribute_name(id);
ALTER TABLE spu_regular_attribute_value ADD CONSTRAINT FK_SPU_REGULAR_ATTR_VALUE_TO_SPU FOREIGN KEY(spu_id) REFERENCES spu(id);

ALTER TABLE spu_sale_attribute_mapping ADD CONSTRAINT FK_SPU_SALE_ATTR_MAPPING_TO_SPU FOREIGN KEY(spu_id) REFERENCES spu(id);
ALTER TABLE spu_sale_attribute_mapping ADD CONSTRAINT FK_SPU_SALE_ATTR_MAPPING_TO_ATTR_NAME FOREIGN KEY(attribute_name_id) REFERENCES attribute_name(id);

ALTER TABLE spu_sale_attribute_value ADD CONSTRAINT FK_SPU_SALE_ATTR_VALUE_TO_MAPPING FOREIGN KEY(spu_sale_attribute_mapping_id) REFERENCES spu_sale_attribute_mapping(id);
ALTER TABLE spu_sale_attribute_combination ADD CONSTRAINT FK_SPU_SALE_ATTR_COMB_TO_SPU FOREIGN KEY(spu_id) REFERENCES spu(id);
ALTER TABLE spu_sale_attribute_combination ADD CONSTRAINT FK_SPU_SALE_ATTR_COMB_TO_SKU FOREIGN KEY(sku_id) REFERENCES sku(id);
