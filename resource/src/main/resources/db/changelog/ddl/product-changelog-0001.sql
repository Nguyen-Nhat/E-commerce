--liquibase formatted sql
--changeset ckxnhat:init-product-service
CREATE TABLE brand (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    slug VARCHAR(255) NOT NULL,
    image_id VARCHAR(255),
    is_published BOOLEAN DEFAULT FALSE,
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
    category_level INT,
    is_published BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(255),
    created_on TIMESTAMP(6),
    last_modified_by VARCHAR(255),
    last_modified_on TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE category_brand_relation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    brand_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
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
    name VARCHAR(255) NOT NULL,
    type SMALLINT NOT NULL, -- 0 sale attr, 1 normal attr, 2 both
    is_required BOOLEAN DEFAULT FALSE,
    has_options BOOLEAN DEFAULT FALSE, -- chọn nhiều giá trị
    PRIMARY KEY (id)
);
CREATE TABLE attribute_name_group_relation(
    id BIGINT NOT NULL AUTO_INCREMENT,
    attribute_name_id BIGINT NOT NULL,
    attribute_group_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE spu (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    thumbnail_id VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    brand_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    sort SMALLINT DEFAULT 0,
    min_price FLOAT DEFAULT 0,
    is_published BOOLEAN DEFAULT FALSE,
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
    spu_id BIGINT NOT NULL,
    price FLOAT DEFAULT 0,
--     stock_quantity BIGINT default 0,
    is_allowed_to_order BOOLEAN,
    is_deleted BOOLEAN default FALSE,
    created_by VARCHAR(255),
    created_on TIMESTAMP(6),
    last_modified_by VARCHAR(255),
    last_modified_on TIMESTAMP(6),
    PRIMARY KEY (id)
);
CREATE TABLE sku_sale_attribute_value (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sku_id BIGINT NOT NULL,
    attribute_name_id BIGINT NOT NULL,
    attribute_value VARCHAR(255) NOT NULL,
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

CREATE TABLE sku_image (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sku_id BIGINT NOT NULL,
    image_id VARCHAR(255),
    PRIMARY KEY (id)
);

ALTER TABLE category ADD CONSTRAINT FK_CATEGORY_TO_CATEGORY FOREIGN KEY(parent_id) REFERENCES category(id);
ALTER TABLE category_brand_relation ADD CONSTRAINT FK_CATE_BRAND_RELA_TO_CATE FOREIGN KEY(category_id) REFERENCES category(id);
ALTER TABLE category_brand_relation ADD CONSTRAINT FK_CATE_BRAND_RELA_TO_BRAND FOREIGN KEY(brand_id) REFERENCES brand(id);
ALTER TABLE attribute_group ADD CONSTRAINT FK_ATTRIBUTE_GROUP_TO_CATEGORY FOREIGN KEY(category_id) REFERENCES category(id);
ALTER TABLE attribute_name_group_relation ADD CONSTRAINT FK_ATTR_NAME_GROUP_RELA_TO_ATTR_GROUP FOREIGN KEY(attribute_group_id) REFERENCES attribute_group(id);
ALTER TABLE attribute_name_group_relation ADD CONSTRAINT FK_ATTR_NAME_GROUP_RELA_TO_ATTR_NAME FOREIGN KEY(attribute_name_id) REFERENCES attribute_name(id);
ALTER TABLE spu ADD CONSTRAINT FK_SPU_TO_CATEGORY FOREIGN KEY(category_id) REFERENCES category(id);
ALTER TABLE spu ADD CONSTRAINT FK_SPU_TO_BRAND FOREIGN KEY(brand_id) REFERENCES brand(id);
ALTER TABLE sku ADD CONSTRAINT FK_SKU_TO_SPU FOREIGN KEY(spu_id) REFERENCES spu(id);
ALTER TABLE sku_image ADD CONSTRAINT FK_SKU_IMAGE_TO_SKU FOREIGN KEY(sku_id) REFERENCES sku(id);
ALTER TABLE spu_description ADD CONSTRAINT FK_SPU_DES_TO_SPU FOREIGN KEY(spu_id) REFERENCES spu(id);
ALTER TABLE spu_regular_attribute_value ADD CONSTRAINT FK_SPU_REGULAR_ATTR_VALUE_TO_ATTR_NAME FOREIGN KEY(attribute_name_id) REFERENCES attribute_name(id);
ALTER TABLE spu_regular_attribute_value ADD CONSTRAINT FK_SPU_REGULAR_ATTR_VALUE_TO_SPU FOREIGN KEY(spu_id) REFERENCES spu(id);
ALTER TABLE sku_sale_attribute_value ADD CONSTRAINT FK_SKU_SALE_ATTR_VALUE_TO_ATTR_NAME FOREIGN KEY(attribute_name_id) REFERENCES attribute_name(id);
ALTER TABLE sku_sale_attribute_value ADD CONSTRAINT FK_SKU_SALE_ATTR_VALUE_TO_SKU FOREIGN KEY(sku_id) REFERENCES sku(id);